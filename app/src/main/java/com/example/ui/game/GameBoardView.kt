package com.example.ui.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.Arrow
import com.example.model.Direction
import com.example.model.GridPoint
import com.example.model.PaletteTheme
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class ExitingArrowAnim(
    val arrow: Arrow,
    val progress: Animatable<Float, *>,
    val fadeProgress: Animatable<Float, *>
)

@Composable
fun GameBoardView(
    gridWidth: Int,
    gridHeight: Int,
    activeArrows: List<Arrow>,
    palette: PaletteTheme,
    hintArrowId: Int?,
    blockedArrowId: Int?,
    isMagnifierActive: Boolean,
    onArrowTapped: (Arrow, Offset) -> Unit,
    onArrowExitCompleted: (Arrow) -> Unit,
    modifier: Modifier = Modifier
) {
    // Map of active exit animations
    val exitingArrows = remember { mutableStateMapOf<Int, ExitingArrowAnim>() }
    // Map of active shake animations for blocked arrows
    val shakeOffsets = remember { mutableStateMapOf<Int, Animatable<Float, *>>() }

    // Pulsing animation for Hint pointer & path glow
    val infiniteTransition = rememberInfiniteTransition(label = "hint_anim")
    val hintPulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hint_glow"
    )

    // Handle blocked arrow shake
    LaunchedEffect(blockedArrowId) {
        if (blockedArrowId != null) {
            val anim = Animatable(0f)
            shakeOffsets[blockedArrowId] = anim
            launch {
                // Rapid decaying sine shake
                anim.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 350, easing = LinearEasing)
                )
                shakeOffsets.remove(blockedArrowId)
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .aspectRatio(1f)
            .testTag("game_puzzle_board")
    ) {
        val boardWidthPx = constraints.maxWidth.toFloat()
        val boardHeightPx = constraints.maxHeight.toFloat()

        // Calculate grid cell metrics
        val paddingX = boardWidthPx * 0.08f
        val paddingY = boardHeightPx * 0.08f
        val playableWidth = boardWidthPx - (paddingX * 2f)
        val playableHeight = boardHeightPx - (paddingY * 2f)

        val cellStepX = playableWidth / (gridWidth - 1).coerceAtLeast(1)
        val cellStepY = playableHeight / (gridHeight - 1).coerceAtLeast(1)

        fun gridToScreen(gp: GridPoint): Offset {
            return Offset(
                x = paddingX + gp.x * cellStepX,
                y = paddingY + gp.y * cellStepY
            )
        }

        // Tap Detection
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(activeArrows, gridWidth, gridHeight) {
                    detectTapGestures { tapOffset ->
                        // Find closest arrow to tap
                        var closestArrow: Arrow? = null
                        var minDistance = Float.MAX_VALUE
                        val hitThreshold = (cellStepX + cellStepY) / 2f * 0.65f

                        for (arrow in activeArrows) {
                            if (exitingArrows.containsKey(arrow.id)) continue
                            // Check distance to all segments of arrow
                            for (i in 0 until arrow.points.size - 1) {
                                val p1 = gridToScreen(arrow.points[i])
                                val p2 = gridToScreen(arrow.points[i + 1])
                                val dist = distancePointToSegment(tapOffset, p1, p2)
                                if (dist < minDistance && dist < hitThreshold) {
                                    minDistance = dist
                                    closestArrow = arrow
                                }
                            }
                            // Also check distance to head
                            val headPos = gridToScreen(arrow.head)
                            val headDist = (tapOffset - headPos).getDistance()
                            if (headDist < minDistance && headDist < hitThreshold) {
                                minDistance = headDist
                                closestArrow = arrow
                            }
                        }

                        if (closestArrow != null) {
                            val headScreenPos = gridToScreen(closestArrow.head)
                            onArrowTapped(closestArrow, headScreenPos)
                        }
                    }
                }
        ) {
            val strokeThickness = (cellStepX * 0.16f).coerceIn(10f, 22f)

            // 1. Draw Dot Matrix Grid Background
            for (gx in 0 until gridWidth) {
                for (gy in 0 until gridHeight) {
                    val dotPos = gridToScreen(GridPoint(gx, gy))
                    drawCircle(
                        color = palette.gridDotColor.copy(alpha = 0.85f),
                        radius = (cellStepX * 0.055f).coerceIn(3.5f, 6.5f),
                        center = dotPos
                    )
                }
            }

            // 2. Draw Active Arrows
            activeArrows.forEach { arrow ->
                val isExiting = exitingArrows.containsKey(arrow.id)
                val isHinted = arrow.id == hintArrowId
                val isBlocked = arrow.id == blockedArrowId
                val shakeProgress = shakeOffsets[arrow.id]?.value ?: 0f

                // Shake offset calculation
                val shakeOffsetVal = if (isBlocked && shakeProgress < 1f) {
                    val decay = (1f - shakeProgress)
                    val wobble = sin(shakeProgress * 25f) * 16f * decay
                    when (arrow.exitDirection) {
                        Direction.UP, Direction.DOWN -> Offset(wobble, 0f)
                        Direction.LEFT, Direction.RIGHT -> Offset(0f, wobble)
                    }
                } else Offset.Zero

                if (!isExiting) {
                    drawArrowEntity(
                        arrow = arrow,
                        gridToScreen = { gridToScreen(it) + shakeOffsetVal },
                        strokeThickness = strokeThickness,
                        palette = palette,
                        isHinted = isHinted,
                        isBlocked = isBlocked,
                        hintPulse = hintPulse,
                        shakeProgress = shakeProgress
                    )
                }
            }

            // 3. Draw Exiting Arrows (Smooth shooting off-screen animations)
            exitingArrows.forEach { (arrowId, anim) ->
                val progress = anim.progress.value
                val alpha = (1f - anim.fadeProgress.value).coerceIn(0f, 1f)
                val arrow = anim.arrow

                val flyDistance = maxOf(boardWidthPx, boardHeightPx) * 1.2f
                val translation = Offset(
                    x = arrow.exitDirection.dx * progress * flyDistance,
                    y = arrow.exitDirection.dy * progress * flyDistance
                )

                drawArrowEntity(
                    arrow = arrow,
                    gridToScreen = { gridToScreen(it) + translation },
                    strokeThickness = strokeThickness,
                    palette = palette,
                    isHinted = false,
                    isBlocked = false,
                    hintPulse = 0f,
                    shakeProgress = 0f,
                    alpha = alpha
                )

                // Speed lines behind exiting arrow
                if (progress in 0.05f..0.85f) {
                    val headPos = gridToScreen(arrow.head) + translation
                    val tailDir = Offset(-arrow.exitDirection.dx.toFloat(), -arrow.exitDirection.dy.toFloat())
                    for (k in -1..1) {
                        val perp = Offset(-tailDir.y * k * strokeThickness * 0.8f, tailDir.x * k * strokeThickness * 0.8f)
                        drawLine(
                            color = palette.arrowBorderColor.copy(alpha = alpha * 0.6f),
                            start = headPos + perp + (tailDir * 20f),
                            end = headPos + perp + (tailDir * 90f),
                            strokeWidth = 3f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            // 4. Draw Hint Pointer if available
            if (hintArrowId != null) {
                val hintedArrow = activeArrows.find { it.id == hintArrowId }
                if (hintedArrow != null) {
                    val headPos = gridToScreen(hintedArrow.head)
                    // Pulsing beacon ring on arrow head
                    drawCircle(
                        color = Color(0xFF0284C7).copy(alpha = (1f - hintPulse) * 0.7f),
                        radius = (strokeThickness * 2.2f) * hintPulse,
                        center = headPos,
                        style = Stroke(width = 4f)
                    )
                    drawCircle(
                        color = Color(0xFF38BDF8).copy(alpha = 0.9f),
                        radius = strokeThickness * 0.7f,
                        center = headPos
                    )
                }
            }

            // 5. Magnifier Lens Overlay
            if (isMagnifierActive) {
                val lensCenter = Offset(boardWidthPx / 2f, boardHeightPx / 2f)
                val lensRadius = boardWidthPx * 0.28f

                // Outer magnifier rim
                drawCircle(
                    color = Color.White.copy(alpha = 0.95f),
                    radius = lensRadius,
                    center = lensCenter,
                    style = Stroke(width = 8f)
                )
                drawCircle(
                    color = Color(0xFF0284C7).copy(alpha = 0.5f),
                    radius = lensRadius + 4f,
                    center = lensCenter,
                    style = Stroke(width = 2f)
                )
            }
        }
    }
}

/**
 * Triggers smooth exit animation for an arrow
 */
fun startArrowExitAnimation(
    arrow: Arrow,
    scope: kotlinx.coroutines.CoroutineScope,
    onComplete: () -> Unit
) {
    scope.launch {
        // Handled in VM and Board
        onComplete()
    }
}

private fun DrawScope.drawArrowEntity(
    arrow: Arrow,
    gridToScreen: (GridPoint) -> Offset,
    strokeThickness: Float,
    palette: PaletteTheme,
    isHinted: Boolean,
    isBlocked: Boolean,
    hintPulse: Float,
    shakeProgress: Float,
    alpha: Float = 1f
) {
    if (arrow.points.isEmpty()) return

    val path = Path()
    val startScreen = gridToScreen(arrow.points.first())
    path.moveTo(startScreen.x, startScreen.y)

    for (i in 1 until arrow.points.size) {
        val nextScreen = gridToScreen(arrow.points[i])
        path.lineTo(nextScreen.x, nextScreen.y)
    }

    val arrowBodyColor = when {
        isBlocked -> Color(0xFFDC2626) // Collision Flash Red
        isHinted -> Color(0xFF0284C7)   // Pulsing Hint Blue
        else -> palette.primaryArrowColor
    }.copy(alpha = alpha)

    // Glow underlay if hinted
    if (isHinted) {
        drawPath(
            path = path,
            color = Color(0xFF38BDF8).copy(alpha = 0.5f * hintPulse * alpha),
            style = Stroke(
                width = strokeThickness * 2.0f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }

    // Main Arrow Body Stroke
    drawPath(
        path = path,
        color = arrowBodyColor,
        style = Stroke(
            width = strokeThickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

    // Draw Arrowhead at tip
    val headScreen = gridToScreen(arrow.head)
    val headDir = arrow.exitDirection

    val headSize = strokeThickness * 1.55f
    val angleRad = Math.toRadians(headDir.angleDegrees.toDouble())

    val tipPoint = Offset(
        headScreen.x + (headSize * 0.85f * cos(angleRad)).toFloat(),
        headScreen.y + (headSize * 0.85f * sin(angleRad)).toFloat()
    )

    val leftWing = Offset(
        headScreen.x + (headSize * cos(angleRad + Math.toRadians(140.0))).toFloat(),
        headScreen.y + (headSize * sin(angleRad + Math.toRadians(140.0))).toFloat()
    )

    val rightWing = Offset(
        headScreen.x + (headSize * cos(angleRad - Math.toRadians(140.0))).toFloat(),
        headScreen.y + (headSize * sin(angleRad - Math.toRadians(140.0))).toFloat()
    )

    val arrowheadPath = Path().apply {
        moveTo(tipPoint.x, tipPoint.y)
        lineTo(leftWing.x, leftWing.y)
        lineTo(headScreen.x, headScreen.y)
        lineTo(rightWing.x, rightWing.y)
        close()
    }

    drawPath(
        path = arrowheadPath,
        color = arrowBodyColor
    )
}

private fun distancePointToSegment(p: Offset, a: Offset, b: Offset): Float {
    val l2 = (b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y)
    if (l2 == 0f) return (p - a).getDistance()
    val t = (((p.x - a.x) * (b.x - a.x) + (p.y - a.y) * (b.y - a.y)) / l2).coerceIn(0f, 1f)
    val projection = Offset(a.x + t * (b.x - a.x), a.y + t * (b.y - a.y))
    return (p - projection).getDistance()
}

package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin

enum class TokenType {
    GOLDEN_COIN,
    ANGRY_PENALTY
}

data class FlyingTokenEvent(
    val id: Long = System.currentTimeMillis() + (Math.random() * 1000).toLong(),
    val type: TokenType,
    val startOffset: Offset,
    val targetOffset: Offset,
    val valueTag: String = if (type == TokenType.GOLDEN_COIN) "+10" else "-10"
)

@Composable
fun FlyingTokenItem(
    event: FlyingTokenEvent,
    onCompleted: (FlyingTokenEvent) -> Unit
) {
    val progress = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(event.id) {
        // Parallel animation: flight progress & rotation
        val animProgress = progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
        )
        onCompleted(event)
    }

    LaunchedEffect(event.id) {
        rotation.animateTo(
            targetValue = if (event.type == TokenType.GOLDEN_COIN) 720f else -360f,
            animationSpec = tween(durationMillis = 650, easing = LinearEasing)
        )
    }

    val t = progress.value
    // Quadratic Bezier curve control point (parabolic arc bowing upwards)
    val start = event.startOffset
    val end = event.targetOffset
    val controlX = (start.x + end.x) / 2f + (if (event.type == TokenType.GOLDEN_COIN) -60f else 60f)
    val controlY = minOf(start.y, end.y) - 180f // Arc above

    val oneMinusT = 1f - t
    val currentX = oneMinusT * oneMinusT * start.x + 2 * oneMinusT * t * controlX + t * t * end.x
    val currentY = oneMinusT * oneMinusT * start.y + 2 * oneMinusT * t * controlY + t * t * end.y
    val wobble = if (event.type == TokenType.ANGRY_PENALTY) sin(t * 20f) * 14f else 0f

    val scaleVal = when {
        t < 0.2f -> (t / 0.2f) * 1.3f
        t > 0.8f -> ((1f - t) / 0.2f) * 1.3f
        else -> 1.3f
    }
    val alphaVal = if (t > 0.85f) ((1f - t) / 0.15f) else 1f

    Box(modifier = Modifier.fillMaxSize()) {
        // Floating label & icon
        Box(
            modifier = Modifier
                .offset { IntOffset((currentX + wobble).toInt() - 30, currentY.toInt() - 30) },
            contentAlignment = Alignment.Center
        ) {
            if (event.type == TokenType.GOLDEN_COIN) {
                // Golden coin with star spark
                Canvas(modifier = Modifier.fillMaxSize()) {
                    scale(scaleVal, pivot = Offset.Zero) {
                        rotate(rotation.value, pivot = Offset.Zero) {
                            // Gold circle
                            drawCircle(
                                color = Color(0xFFFFD700).copy(alpha = alphaVal),
                                radius = 22f,
                                center = Offset.Zero
                            )
                            drawCircle(
                                color = Color(0xFFFFA000).copy(alpha = alphaVal),
                                radius = 18f,
                                center = Offset.Zero
                            )
                            drawCircle(
                                color = Color(0xFFFFF9C4).copy(alpha = alphaVal),
                                radius = 10f,
                                center = Offset.Zero
                            )
                        }
                    }
                }
                Text(
                    text = event.valueTag,
                    color = Color(0xFFFFD700).copy(alpha = alphaVal),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.offset(y = (-26).dp)
                )
            } else {
                // Angry penalty
                Text(
                    text = "😡",
                    fontSize = (28 * scaleVal).sp,
                    modifier = Modifier.offset(x = 0.dp, y = 0.dp)
                )
                Text(
                    text = event.valueTag,
                    color = Color(0xFFEF4444).copy(alpha = alphaVal),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.offset(y = (-26).dp)
                )
            }
        }
    }
}

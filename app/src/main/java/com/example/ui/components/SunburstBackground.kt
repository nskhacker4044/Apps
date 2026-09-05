package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun SunburstBackground(
    modifier: Modifier = Modifier,
    rayColors: List<Color> = listOf(Color(0xFF0284C7), Color(0xFF38BDF8), Color(0xFF0369A1)),
    rayCount: Int = 24
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sunburst_rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sunburst_angle"
    )

    val baseColor1 = rayColors.getOrElse(0) { Color(0xFF0284C7) }
    val baseColor2 = rayColors.getOrElse(1) { Color(0xFF38BDF8) }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = sqrt(size.width * size.width + size.height * size.height)

        // Draw radial background gradient
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(baseColor2.copy(alpha = 0.95f), baseColor1),
                center = center,
                radius = radius * 0.7f
            ),
            size = size
        )

        // Draw rotating sunburst rays
        rotate(degrees = rotationAngle, pivot = center) {
            val angleStep = 360f / rayCount
            for (i in 0 until rayCount step 2) {
                val startRad = Math.toRadians((i * angleStep).toDouble())
                val endRad = Math.toRadians(((i + 1) * angleStep).toDouble())

                val p1 = Offset(
                    center.x + (radius * cos(startRad)).toFloat(),
                    center.y + (radius * sin(startRad)).toFloat()
                )
                val p2 = Offset(
                    center.x + (radius * cos(endRad)).toFloat(),
                    center.y + (radius * sin(endRad)).toFloat()
                )

                val path = Path().apply {
                    moveTo(center.x, center.y)
                    lineTo(p1.x, p1.y)
                    lineTo(p2.x, p2.y)
                    close()
                }

                drawPath(
                    path = path,
                    color = Color.White.copy(alpha = 0.14f)
                )
            }
        }
    }
}

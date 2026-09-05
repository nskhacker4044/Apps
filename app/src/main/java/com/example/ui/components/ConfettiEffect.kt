package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class ConfettiParticle(
    val startX: Float,
    val startY: Float,
    val velocityX: Float,
    val velocityY: Float,
    val color: Color,
    val width: Float,
    val height: Float,
    val rotationSpeed: Float,
    val wobbleSpeed: Float
)

@Composable
fun ConfettiEffect(
    modifier: Modifier = Modifier,
    particleCount: Int = 75
) {
    val progress = remember { Animatable(0f) }

    val colors = listOf(
        Color(0xFFFF3366),
        Color(0xFF33CCFF),
        Color(0xFFFFCC00),
        Color(0xFF33FF99),
        Color(0xFF9933FF),
        Color(0xFFFF9933),
        Color(0xFF38BDF8),
        Color(0xFFF43F5E)
    )

    val particles = remember {
        List(particleCount) {
            val angle = Random.nextFloat() * 2f * Math.PI.toFloat()
            val speed = Random.nextFloat() * 600f + 200f
            ConfettiParticle(
                startX = 0.5f + (Random.nextFloat() - 0.5f) * 0.4f,
                startY = 0.35f + (Random.nextFloat() - 0.5f) * 0.2f,
                velocityX = cos(angle) * speed,
                velocityY = sin(angle) * speed - 350f, // initial upward thrust
                color = colors.random(),
                width = Random.nextFloat() * 12f + 10f,
                height = Random.nextFloat() * 8f + 6f,
                rotationSpeed = (Random.nextFloat() - 0.5f) * 720f,
                wobbleSpeed = Random.nextFloat() * 10f + 5f
            )
        }
    }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 3000, easing = LinearEasing)
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val t = progress.value
        val gravity = 900f

        particles.forEach { p ->
            val timeSec = t * 3.0f
            val currentX = p.startX * size.width + p.velocityX * timeSec * 0.8f + sin(timeSec * p.wobbleSpeed) * 20f
            val currentY = p.startY * size.height + p.velocityY * timeSec + 0.5f * gravity * timeSec * timeSec
            val rotation = p.rotationSpeed * timeSec
            val alpha = (1f - (t - 0.6f).coerceAtLeast(0f) / 0.4f).coerceIn(0f, 1f)

            if (alpha > 0.01f && currentY in -50f..size.height + 50f && currentX in -50f..size.width + 50f) {
                rotate(degrees = rotation, pivot = Offset(currentX, currentY)) {
                    drawRect(
                        color = p.color.copy(alpha = alpha),
                        topLeft = Offset(currentX - p.width / 2f, currentY - p.height / 2f),
                        size = Size(p.width, p.height)
                    )
                }
            }
        }
    }
}

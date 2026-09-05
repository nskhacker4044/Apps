package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameDifficulty

@Composable
fun GameHUD(
    levelNumber: Int,
    levelTitle: String,
    activeArrowsCount: Int,
    timeRemainingSeconds: Int,
    livesRemaining: Int,
    difficulty: GameDifficulty,
    score: Int,
    onScoreBadgePositioned: (Offset) -> Unit,
    onBackClick: () -> Unit,
    onRestartClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Row 1: Back, Title, Settings & Restart
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
                    .shadow(2.dp, CircleShape)
                    .testTag("hud_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Level Select",
                    tint = Color(0xFF0F172A),
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Level $levelNumber",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = levelTitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onRestartClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .shadow(2.dp, CircleShape)
                        .testTag("hud_restart_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Restart Level",
                        tint = Color(0xFF0F172A),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .shadow(2.dp, CircleShape)
                        .testTag("hud_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color(0xFF0F172A),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Row 2: Active Arrows, Lives, Difficulty Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Active arrows counter badge
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF1F5F9),
                shadowElevation = 1.dp,
                modifier = Modifier.testTag("active_arrows_badge")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🚀", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "$activeArrowsCount",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }
            }

            // 3 Hearts / Lives indicator with animated broken heart transitions
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.testTag("lives_indicator")
            ) {
                for (i in 1..3) {
                    val isAlive = i <= livesRemaining
                    AnimatedContent(
                        targetState = isAlive,
                        transitionSpec = {
                            (scaleIn(tween(250)) + fadeIn()) togetherWith (scaleOut(tween(250)) + fadeOut())
                        },
                        label = "heart_anim_$i"
                    ) { alive ->
                        Text(
                            text = if (alive) "❤️" else "💔",
                            fontSize = 20.sp
                        )
                    }
                }
            }

            // Difficulty pill
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = difficulty.badgeColor.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, difficulty.badgeColor.copy(alpha = 0.5f)),
                modifier = Modifier.testTag("difficulty_pill")
            ) {
                Text(
                    text = difficulty.label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = difficulty.badgeColor,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 3: Countdown Timer & Golden Points Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 60-Second Countdown Timer with dynamic color transitions
            CountdownTimerBadge(timeRemainingSeconds = timeRemainingSeconds)

            // Golden Shiny Points Badge
            GoldenPointsBadge(
                score = score,
                onPositioned = onScoreBadgePositioned
            )
        }
    }
}

@Composable
fun CountdownTimerBadge(
    timeRemainingSeconds: Int,
    modifier: Modifier = Modifier
) {
    val timerColor by animateColorAsState(
        targetValue = when {
            timeRemainingSeconds <= 10 -> Color(0xFFEF4444) // Urgent Red
            timeRemainingSeconds <= 30 -> Color(0xFFF59E0B) // Warning Orange
            else -> Color(0xFF1E3A8A)                       // Dark Blue
        },
        animationSpec = tween(400),
        label = "timer_color"
    )

    // Pulse animation when time is critically low (<= 10s)
    val infiniteTransition = rememberInfiniteTransition(label = "timer_pulse")
    val pulseScale by if (timeRemainingSeconds <= 10) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.12f,
            animationSpec = infiniteRepeatable(
                animation = tween(400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_scale"
        )
    } else {
        rememberInfiniteTransition(label = "static").animateFloat(
            initialValue = 1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(1000)),
            label = "static_scale"
        )
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = timerColor.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, timerColor),
        modifier = modifier
            .scale(pulseScale)
            .testTag("countdown_timer_badge")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (timeRemainingSeconds <= 10) "🔥" else "⏱️",
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            val minutes = timeRemainingSeconds / 60
            val seconds = timeRemainingSeconds % 60
            val timeString = String.format("%02d:%02d", minutes, seconds)
            Text(
                text = timeString,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = timerColor
            )
        }
    }
}

@Composable
fun GoldenPointsBadge(
    score: Int,
    onPositioned: (Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "golden_shimmer")
    val shimmerTranslate by infiniteTransition.animateFloat(
        initialValue = -100f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_pos"
    )

    val goldenBorder = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFD700),
            Color(0xFFFFF9C4),
            Color(0xFFFFA000),
            Color(0xFFFFD700)
        )
    )

    Box(
        modifier = modifier
            .onGloballyPositioned { coords ->
                onPositioned(coords.positionInWindow())
            }
            .clip(RoundedCornerShape(16.dp))
            .border(1.8.dp, goldenBorder, RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFFFFFBEB), Color(0xFFFEF3C7))
                )
            )
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag("golden_points_badge"),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🪙", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            AnimatedContent(
                targetState = score,
                transitionSpec = {
                    (fadeIn(tween(150)) + scaleIn(tween(150))) togetherWith
                            (fadeOut(tween(150)) + scaleOut(tween(150)))
                },
                label = "score_anim"
            ) { currScore ->
                Text(
                    text = "$currScore pts",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFB45309)
                )
            }
        }
    }
}

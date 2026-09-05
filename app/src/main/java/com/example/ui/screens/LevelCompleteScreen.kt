package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ConfettiEffect
import com.example.ui.components.SunburstBackground
import com.example.viewmodel.VictoryStats

@Composable
fun LevelCompleteScreen(
    stats: VictoryStats,
    onNextLevel: () -> Unit,
    onMainSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val star1Scale = remember { Animatable(0f) }
    val star2Scale = remember { Animatable(0f) }
    val star3Scale = remember { Animatable(0f) }
    val cardScale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        cardScale.animateTo(1f, tween(400, easing = FastOutSlowInEasing))
        if (stats.stars >= 1) {
            star1Scale.animateTo(1f, tween(300, easing = FastOutSlowInEasing))
        }
        if (stats.stars >= 2) {
            star2Scale.animateTo(1f, tween(300, easing = FastOutSlowInEasing))
        }
        if (stats.stars >= 3) {
            star3Scale.animateTo(1f, tween(300, easing = FastOutSlowInEasing))
        }
    }

    val palette = stats.levelConfig.defaultPalette

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 1. Dynamic Radiant Sunburst Rays Background matching the level's color theme
        SunburstBackground(rayColors = palette.sunburstRayColors)

        // 2. Celebration Confetti Burst
        ConfettiEffect()

        // 3. Main Content Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Titles
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                // "Train Your Brain" in bold Black
                Text(
                    text = "Train Your Brain",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                // "Level X Completed!" in Dark Red (#8B0000)
                Text(
                    text = "Level ${stats.levelNumber} Completed!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF8B0000), // Dark Red
                    textAlign = TextAlign.Center
                )
            }

            // Performance Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(cardScale.value)
                    .shadow(16.dp, RoundedCornerShape(28.dp))
                    .testTag("level_complete_summary_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 3 Stars Display with sequential pop animation
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.scale(star1Scale.value)) {
                            Text(text = "⭐", fontSize = 38.sp)
                        }
                        Box(modifier = Modifier.scale(star2Scale.value)) {
                            Text(text = if (stats.stars >= 2) "⭐" else "☆", fontSize = 48.sp)
                        }
                        Box(modifier = Modifier.scale(star3Scale.value)) {
                            Text(text = if (stats.stars >= 3) "⭐" else "☆", fontSize = 38.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Stats row: Total Points + Completion Time
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Score",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = "${stats.score} pts 🪙",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFD97706)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Time",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = "${stats.timeTakenSeconds}s ⏱️",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0284C7)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bonus speed callout
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFEF3C7),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Text(
                            text = stats.bonusSpeedText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // Bottom Navigation Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // "New Game" / "Next Level" Primary Stadium Button
                Button(
                    onClick = onNextLevel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF0284C7)
                    ),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(56.dp)
                        .shadow(8.dp, RoundedCornerShape(30.dp))
                        .testTag("victory_next_level_button")
                ) {
                    Text(
                        text = "New Game",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0284C7)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // "Main" Navigation Button rendered in bold Black
                Text(
                    text = "Main",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    modifier = Modifier
                        .clickable { onMainSelect() }
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .testTag("victory_main_button")
                )
            }
        }
    }
}

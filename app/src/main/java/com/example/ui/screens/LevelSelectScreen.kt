package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LevelDefinitions
import com.example.data.entity.GameStats
import com.example.data.entity.LevelProgress
import com.example.model.LevelConfig

@Composable
fun LevelSelectScreen(
    progressList: List<LevelProgress>,
    stats: GameStats?,
    onLevelSelected: (Int) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progressMap = progressList.associateBy { it.levelNumber }
    val totalCoins = stats?.totalCoins ?: 0
    val totalStars = progressList.sumOf { it.starsEarned }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF0F172A))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Tap Away Arrows",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Select a Puzzle Level",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF38BDF8)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Star & Coin pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF1E293B),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⭐ $totalStars", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "🪙 $totalCoins", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF59E0B))
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF334155))
                            .testTag("level_select_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4x5 Grid of 20 Levels
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(LevelDefinitions.levels) { levelConfig ->
                    val progress = progressMap[levelConfig.levelNumber]
                    val isUnlocked = progress?.isUnlocked ?: (levelConfig.levelNumber == 1)
                    val stars = progress?.starsEarned ?: 0

                    LevelCard(
                        levelConfig = levelConfig,
                        isUnlocked = isUnlocked,
                        stars = stars,
                        highScore = progress?.highScore ?: 0,
                        onClick = {
                            if (isUnlocked) {
                                onLevelSelected(levelConfig.levelNumber)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LevelCard(
    levelConfig: LevelConfig,
    isUnlocked: Boolean,
    stars: Int,
    highScore: Int,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) Color(0xFF1E293B) else Color(0xFF0F172A)
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isUnlocked) 1.5.dp else 1.dp,
            color = if (isUnlocked) Color(0xFF38BDF8).copy(alpha = 0.6f) else Color(0xFF334155)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isUnlocked) 6.dp else 0.dp, RoundedCornerShape(16.dp))
            .clickable(enabled = isUnlocked) { onClick() }
            .testTag("level_card_${levelConfig.levelNumber}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isUnlocked) {
                // Level Number
                Text(
                    text = "${levelConfig.levelNumber}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Text(
                    text = levelConfig.title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF94A3B8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Stars display (1..3)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (s in 1..3) {
                        Text(
                            text = if (s <= stars) "⭐" else "☆",
                            fontSize = 11.sp,
                            color = if (s <= stars) Color(0xFFFFD700) else Color(0xFF64748B)
                        )
                    }
                }
            } else {
                // Locked
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked Level",
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${levelConfig.levelNumber}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PaletteTheme

@Composable
fun ToolButtons(
    hintsCount: Int,
    isMagnifierActive: Boolean,
    isPaletteMenuOpen: Boolean,
    selectedPalette: PaletteTheme,
    onHintClick: () -> Unit,
    onMagnifierToggle: () -> Unit,
    onPaletteToggle: () -> Unit,
    onPaletteSelect: (PaletteTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Expandable Palette Switcher Row
        AnimatedVisibility(
            visible = isPaletteMenuOpen,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200))
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth()
                    .testTag("palette_selector_bar")
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PaletteTheme.allThemes.forEach { theme ->
                        val isSelected = theme.id == selectedPalette.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(theme.primaryArrowColor, theme.secondaryArrowColor)
                                    )
                                )
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) Color(0xFFFFD700) else Color.Transparent,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { onPaletteSelect(theme) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("palette_option_${theme.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = theme.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Main Bottom Tool Circular Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 💡 Hint Tool Button with Count Badge
            HintToolButton(
                hintsCount = hintsCount,
                onClick = onHintClick
            )

            // 🔍 Magnifier Glass Tool
            CircularToolButton(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Magnifier Inspection Lens",
                        tint = if (isMagnifierActive) Color.White else Color(0xFF0284C7),
                        modifier = Modifier.size(28.dp)
                    )
                },
                isActive = isMagnifierActive,
                activeBackgroundColor = Color(0xFF0284C7),
                testTag = "tool_magnifier_button",
                onClick = onMagnifierToggle
            )

            // 🎨 Palette Switcher Tool
            CircularToolButton(
                icon = {
                    Icon(
                        imageVector = Icons.Default.ColorLens,
                        contentDescription = "Change Color Theme",
                        tint = if (isPaletteMenuOpen) Color.White else Color(0xFF8B5CF6),
                        modifier = Modifier.size(28.dp)
                    )
                },
                isActive = isPaletteMenuOpen,
                activeBackgroundColor = Color(0xFF8B5CF6),
                testTag = "tool_palette_button",
                onClick = onPaletteToggle
            )
        }
    }
}

@Composable
fun HintToolButton(
    hintsCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hint_glow")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hint_pulse"
    )

    Box(
        modifier = modifier
            .scale(pulseScale)
            .testTag("tool_hint_button"),
        contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFFFFFFF), Color(0xFFE0F2FE))
                    )
                )
                .border(2.dp, Color(0xFF0284C7).copy(alpha = 0.6f), CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = "Use Hint",
                tint = Color(0xFF0284C7),
                modifier = Modifier.size(32.dp)
            )
        }

        // Available Hint Count Badge
        Box(
            modifier = Modifier
                .offset(x = 4.dp, y = (-4).dp)
                .size(24.dp)
                .shadow(2.dp, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFF0284C7)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$hintsCount",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
    }
}

@Composable
fun CircularToolButton(
    icon: @Composable () -> Unit,
    isActive: Boolean,
    activeBackgroundColor: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(60.dp)
            .shadow(5.dp, CircleShape)
            .clip(CircleShape)
            .background(
                if (isActive) activeBackgroundColor
                else Color.White
            )
            .border(
                1.5.dp,
                if (isActive) activeBackgroundColor else Color(0xFFE2E8F0),
                CircleShape
            )
            .clickable { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

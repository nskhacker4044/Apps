package com.example.model

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

enum class Direction(val dx: Int, val dy: Int, val angleDegrees: Float) {
    UP(0, -1, 270f),
    RIGHT(1, 0, 0f),
    DOWN(0, 1, 90f),
    LEFT(-1, 0, 180f);

    companion object {
        fun fromDelta(dx: Int, dy: Int): Direction {
            return when {
                dx > 0 -> RIGHT
                dx < 0 -> LEFT
                dy > 0 -> DOWN
                else -> UP
            }
        }
    }
}

data class GridPoint(
    val x: Int,
    val y: Int
)

data class Arrow(
    val id: Int,
    /**
     * Sequence of grid points from tail to head.
     * The last element is the arrow tip/head.
     */
    val points: List<GridPoint>,
    /**
     * Facing direction of the arrowhead (where it will fly).
     */
    val exitDirection: Direction,
    /**
     * Visual style / color accent override if any.
     */
    val customColorHex: Long? = null
) {
    val head: GridPoint get() = points.last()
    val tail: GridPoint get() = points.first()

    /**
     * Returns whether any part of this arrow body or head occupies grid point (x, y).
     */
    fun occupies(x: Int, y: Int): Boolean {
        return points.any { it.x == x && it.y == y }
    }
}

enum class GameDifficulty(val label: String, val badgeColor: Color) {
    EASY("Easy", Color(0xFF10B981)),
    NORMAL("Normal", Color(0xFF3B82F6)),
    HARD("Hard", Color(0xFFF59E0B)),
    EXPERT("Expert", Color(0xFFEF4444))
}

data class PaletteTheme(
    val id: String,
    val name: String,
    val primaryArrowColor: Color,
    val secondaryArrowColor: Color,
    val arrowBorderColor: Color,
    val gridDotColor: Color,
    val backgroundGradient: List<Color>,
    val accentGlowColor: Color,
    val sunburstRayColors: List<Color>
) {
    companion object {
        val ClassicIndigo = PaletteTheme(
            id = "classic_indigo",
            name = "Classic Indigo",
            primaryArrowColor = Color(0xFF0F172A),
            secondaryArrowColor = Color(0xFF1E293B),
            arrowBorderColor = Color(0xFF38BDF8),
            gridDotColor = Color(0xFFCBD5E1),
            backgroundGradient = listOf(Color(0xFFF8FAFC), Color(0xFFE2E8F0)),
            accentGlowColor = Color(0xFF0284C7),
            sunburstRayColors = listOf(Color(0xFF0284C7), Color(0xFF38BDF8), Color(0xFF0369A1))
        )

        val EmeraldForest = PaletteTheme(
            id = "emerald_forest",
            name = "Emerald Forest",
            primaryArrowColor = Color(0xFF064E3B),
            secondaryArrowColor = Color(0xFF047857),
            arrowBorderColor = Color(0xFF34D399),
            gridDotColor = Color(0xFFA7F3D0),
            backgroundGradient = listOf(Color(0xFFECFDF5), Color(0xFFD1FAE5)),
            accentGlowColor = Color(0xFF10B981),
            sunburstRayColors = listOf(Color(0xFF10B981), Color(0xFF34D399), Color(0xFF059669))
        )

        val CyberViolet = PaletteTheme(
            id = "cyber_violet",
            name = "Cyber Violet",
            primaryArrowColor = Color(0xFF3B0764),
            secondaryArrowColor = Color(0xFF6B21A8),
            arrowBorderColor = Color(0xFFC084FC),
            gridDotColor = Color(0xFFE9D5FF),
            backgroundGradient = listOf(Color(0xFFFAF5FF), Color(0xFFF3E8FF)),
            accentGlowColor = Color(0xFFA855F7),
            sunburstRayColors = listOf(Color(0xFFA855F7), Color(0xFFC084FC), Color(0xFF7E22CE))
        )

        val SunsetCrimson = PaletteTheme(
            id = "sunset_crimson",
            name = "Sunset Crimson",
            primaryArrowColor = Color(0xFF7F1D1D),
            secondaryArrowColor = Color(0xFFB91C1C),
            arrowBorderColor = Color(0xFFF87171),
            gridDotColor = Color(0xFFFECACA),
            backgroundGradient = listOf(Color(0xFFFFF1F2), Color(0xFFFFE4E6)),
            accentGlowColor = Color(0xFFEF4444),
            sunburstRayColors = listOf(Color(0xFFEF4444), Color(0xFFF87171), Color(0xFFB91C1C))
        )

        val OceanCyan = PaletteTheme(
            id = "ocean_cyan",
            name = "Ocean Cyan",
            primaryArrowColor = Color(0xFF164E63),
            secondaryArrowColor = Color(0xFF0E7490),
            arrowBorderColor = Color(0xFF22D3EE),
            gridDotColor = Color(0xFFCFFAFE),
            backgroundGradient = listOf(Color(0xFFECFEFF), Color(0xFFCFFAFE)),
            accentGlowColor = Color(0xFF06B6D4),
            sunburstRayColors = listOf(Color(0xFF06B6D4), Color(0xFF22D3EE), Color(0xFF0891B2))
        )

        val ObsidianGold = PaletteTheme(
            id = "obsidian_gold",
            name = "Obsidian Gold",
            primaryArrowColor = Color(0xFF1C1917),
            secondaryArrowColor = Color(0xFF292524),
            arrowBorderColor = Color(0xFFFBBF24),
            gridDotColor = Color(0xFFFEF3C7),
            backgroundGradient = listOf(Color(0xFFFFFBEB), Color(0xFFFEF3C7)),
            accentGlowColor = Color(0xFFF59E0B),
            sunburstRayColors = listOf(Color(0xFFF59E0B), Color(0xFFFBBF24), Color(0xFFD97706))
        )

        val allThemes = listOf(
            ClassicIndigo,
            EmeraldForest,
            CyberViolet,
            SunsetCrimson,
            OceanCyan,
            ObsidianGold
        )
    }
}

data class LevelConfig(
    val levelNumber: Int,
    val title: String,
    val subtitle: String,
    val gridWidth: Int,
    val gridHeight: Int,
    val difficulty: GameDifficulty,
    val timeLimitSeconds: Int = 60,
    val initialArrows: List<Arrow>,
    val defaultPalette: PaletteTheme = PaletteTheme.ClassicIndigo
)

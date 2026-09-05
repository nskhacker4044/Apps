package com.example.data

import com.example.model.Arrow
import com.example.model.Direction
import com.example.model.GameDifficulty
import com.example.model.GridPoint
import com.example.model.LevelConfig
import com.example.model.PaletteTheme

object LevelDefinitions {

    val levels: List<LevelConfig> by lazy {
        listOf(
            createLevel1(),
            createLevel2(),
            createLevel3(),
            createLevel4(),
            createLevel5(),
            createLevel6(),
            createLevel7(),
            createLevel8(),
            createLevel9(),
            createLevel10(),
            createLevel11(),
            createLevel12(),
            createLevel13(),
            createLevel14(),
            createLevel15(),
            createLevel16(),
            createLevel17(),
            createLevel18(),
            createLevel19(),
            createLevel20()
        )
    }

    fun getLevel(number: Int): LevelConfig {
        return levels.find { it.levelNumber == number } ?: levels.first()
    }

    // Level 1: First Steps (2x2)
    private fun createLevel1(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(0, 0), GridPoint(1, 0)), Direction.RIGHT),
            Arrow(2, listOf(GridPoint(0, 1), GridPoint(0, 0)), Direction.UP),
            Arrow(3, listOf(GridPoint(1, 1), GridPoint(0, 1)), Direction.LEFT)
        )
        return LevelConfig(
            levelNumber = 1,
            title = "First Steps",
            subtitle = "Tap the free arrows to release them",
            gridWidth = 3,
            gridHeight = 3,
            difficulty = GameDifficulty.EASY,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.ClassicIndigo
        )
    }

    // Level 2: Crossroads (3x3)
    private fun createLevel2(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(1, 0), GridPoint(2, 0)), Direction.RIGHT),
            Arrow(2, listOf(GridPoint(2, 1), GridPoint(2, 2)), Direction.DOWN),
            Arrow(3, listOf(GridPoint(1, 2), GridPoint(0, 2)), Direction.LEFT),
            Arrow(4, listOf(GridPoint(0, 1), GridPoint(0, 0)), Direction.UP),
            Arrow(5, listOf(GridPoint(1, 1), GridPoint(1, 0)), Direction.UP)
        )
        return LevelConfig(
            levelNumber = 2,
            title = "Crossroads",
            subtitle = "Follow the outward escape paths",
            gridWidth = 4,
            gridHeight = 4,
            difficulty = GameDifficulty.EASY,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.EmeraldForest
        )
    }

    // Level 3: Box Trap (4x4)
    private fun createLevel3(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(1, 0), GridPoint(2, 0), GridPoint(3, 0)), Direction.RIGHT),
            Arrow(2, listOf(GridPoint(3, 1), GridPoint(3, 2), GridPoint(3, 3)), Direction.DOWN),
            Arrow(3, listOf(GridPoint(2, 3), GridPoint(1, 3), GridPoint(0, 3)), Direction.LEFT),
            Arrow(4, listOf(GridPoint(0, 2), GridPoint(0, 1), GridPoint(0, 0)), Direction.UP),
            Arrow(5, listOf(GridPoint(1, 1), GridPoint(2, 1)), Direction.RIGHT),
            Arrow(6, listOf(GridPoint(2, 2), GridPoint(1, 2)), Direction.LEFT)
        )
        return LevelConfig(
            levelNumber = 3,
            title = "Box Trap",
            subtitle = "Untangle the nested loops",
            gridWidth = 5,
            gridHeight = 5,
            difficulty = GameDifficulty.EASY,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.OceanCyan
        )
    }

    // Level 4: Snake Weave (4x4)
    private fun createLevel4(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(0, 0), GridPoint(1, 0), GridPoint(1, 1)), Direction.DOWN),
            Arrow(2, listOf(GridPoint(2, 0), GridPoint(3, 0)), Direction.RIGHT),
            Arrow(3, listOf(GridPoint(3, 2), GridPoint(3, 1), GridPoint(2, 1)), Direction.LEFT),
            Arrow(4, listOf(GridPoint(0, 1), GridPoint(0, 2), GridPoint(1, 2)), Direction.RIGHT),
            Arrow(5, listOf(GridPoint(2, 3), GridPoint(1, 3), GridPoint(0, 3)), Direction.LEFT),
            Arrow(6, listOf(GridPoint(3, 3), GridPoint(2, 2)), Direction.UP),
            Arrow(7, listOf(GridPoint(1, 1), GridPoint(2, 1)), Direction.RIGHT)
        )
        return LevelConfig(
            levelNumber = 4,
            title = "Snake Weave",
            subtitle = "Notice each arrow's head",
            gridWidth = 5,
            gridHeight = 5,
            difficulty = GameDifficulty.EASY,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.CyberViolet
        )
    }

    // Level 5: Pyramid Peak (5x5)
    private fun createLevel5(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(2, 0)), Direction.UP),
            Arrow(2, listOf(GridPoint(1, 1), GridPoint(0, 1)), Direction.LEFT),
            Arrow(3, listOf(GridPoint(3, 1), GridPoint(4, 1)), Direction.RIGHT),
            Arrow(4, listOf(GridPoint(0, 2), GridPoint(0, 3)), Direction.DOWN),
            Arrow(5, listOf(GridPoint(4, 2), GridPoint(4, 3)), Direction.DOWN),
            Arrow(6, listOf(GridPoint(1, 2), GridPoint(2, 2)), Direction.RIGHT),
            Arrow(7, listOf(GridPoint(3, 2), GridPoint(3, 3)), Direction.DOWN),
            Arrow(8, listOf(GridPoint(1, 3), GridPoint(2, 3), GridPoint(2, 4)), Direction.DOWN),
            Arrow(9, listOf(GridPoint(2, 1), GridPoint(2, 0)), Direction.UP)
        )
        return LevelConfig(
            levelNumber = 5,
            title = "Pyramid Peak",
            subtitle = "Dismantle from the outside in",
            gridWidth = 5,
            gridHeight = 5,
            difficulty = GameDifficulty.NORMAL,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.SunsetCrimson
        )
    }

    // Level 6: The Spiral (5x5)
    private fun createLevel6(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(0, 0), GridPoint(1, 0), GridPoint(2, 0), GridPoint(3, 0), GridPoint(4, 0)), Direction.RIGHT),
            Arrow(2, listOf(GridPoint(4, 1), GridPoint(4, 2), GridPoint(4, 3), GridPoint(4, 4)), Direction.DOWN),
            Arrow(3, listOf(GridPoint(3, 4), GridPoint(2, 4), GridPoint(1, 4), GridPoint(0, 4)), Direction.LEFT),
            Arrow(4, listOf(GridPoint(0, 3), GridPoint(0, 2), GridPoint(0, 1)), Direction.UP),
            Arrow(5, listOf(GridPoint(1, 1), GridPoint(2, 1), GridPoint(3, 1)), Direction.RIGHT),
            Arrow(6, listOf(GridPoint(3, 2), GridPoint(3, 3)), Direction.DOWN),
            Arrow(7, listOf(GridPoint(2, 3), GridPoint(1, 3)), Direction.LEFT),
            Arrow(8, listOf(GridPoint(1, 2), GridPoint(2, 2)), Direction.RIGHT)
        )
        return LevelConfig(
            levelNumber = 6,
            title = "The Spiral",
            subtitle = "Unwind the outer layer first",
            gridWidth = 5,
            gridHeight = 5,
            difficulty = GameDifficulty.NORMAL,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.ObsidianGold
        )
    }

    // Level 7: Diamond Matrix (6x6)
    private fun createLevel7(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(2, 0), GridPoint(3, 0)), Direction.RIGHT),
            Arrow(2, listOf(GridPoint(4, 1), GridPoint(5, 1)), Direction.RIGHT),
            Arrow(3, listOf(GridPoint(5, 2), GridPoint(5, 3)), Direction.DOWN),
            Arrow(4, listOf(GridPoint(4, 4), GridPoint(5, 4)), Direction.RIGHT),
            Arrow(5, listOf(GridPoint(3, 5), GridPoint(2, 5)), Direction.LEFT),
            Arrow(6, listOf(GridPoint(1, 4), GridPoint(0, 4)), Direction.LEFT),
            Arrow(7, listOf(GridPoint(0, 3), GridPoint(0, 2)), Direction.UP),
            Arrow(8, listOf(GridPoint(1, 1), GridPoint(0, 1)), Direction.LEFT),
            Arrow(9, listOf(GridPoint(2, 1), GridPoint(3, 1)), Direction.RIGHT),
            Arrow(10, listOf(GridPoint(4, 2), GridPoint(4, 3)), Direction.DOWN),
            Arrow(11, listOf(GridPoint(3, 4), GridPoint(2, 4)), Direction.LEFT),
            Arrow(12, listOf(GridPoint(1, 3), GridPoint(1, 2)), Direction.UP)
        )
        return LevelConfig(
            levelNumber = 7,
            title = "Diamond Matrix",
            subtitle = "Radiating geometric symmetry",
            gridWidth = 6,
            gridHeight = 6,
            difficulty = GameDifficulty.NORMAL,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.ClassicIndigo
        )
    }

    // Level 8: Heart Shape (6x6) - matching aa2.webp style
    private fun createLevel8(): LevelConfig {
        val arrows = listOf(
            // Top arches
            Arrow(1, listOf(GridPoint(1, 0), GridPoint(0, 0)), Direction.LEFT),
            Arrow(2, listOf(GridPoint(2, 0), GridPoint(2, 1)), Direction.DOWN),
            Arrow(3, listOf(GridPoint(3, 0), GridPoint(3, 1)), Direction.DOWN),
            Arrow(4, listOf(GridPoint(4, 0), GridPoint(5, 0)), Direction.RIGHT),
            // Upper sides
            Arrow(5, listOf(GridPoint(0, 1), GridPoint(0, 2)), Direction.DOWN),
            Arrow(6, listOf(GridPoint(5, 1), GridPoint(5, 2)), Direction.DOWN),
            // Middle ribs
            Arrow(7, listOf(GridPoint(1, 1), GridPoint(1, 2)), Direction.DOWN),
            Arrow(8, listOf(GridPoint(4, 1), GridPoint(4, 2)), Direction.DOWN),
            Arrow(9, listOf(GridPoint(1, 3), GridPoint(0, 3)), Direction.LEFT),
            Arrow(10, listOf(GridPoint(4, 3), GridPoint(5, 3)), Direction.RIGHT),
            Arrow(11, listOf(GridPoint(2, 2), GridPoint(3, 2)), Direction.RIGHT),
            Arrow(12, listOf(GridPoint(2, 3), GridPoint(3, 3)), Direction.RIGHT),
            // Bottom taper
            Arrow(13, listOf(GridPoint(1, 4), GridPoint(2, 4), GridPoint(2, 5)), Direction.DOWN),
            Arrow(14, listOf(GridPoint(4, 4), GridPoint(3, 4), GridPoint(3, 5)), Direction.DOWN)
        )
        return LevelConfig(
            levelNumber = 8,
            title = "Heart Shape",
            subtitle = "Find the free outer paths",
            gridWidth = 6,
            gridHeight = 6,
            difficulty = GameDifficulty.NORMAL,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.SunsetCrimson
        )
    }

    // Level 9: Castle Gate (6x6)
    private fun createLevel9(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(0, 0), GridPoint(0, 1)), Direction.DOWN),
            Arrow(2, listOf(GridPoint(5, 0), GridPoint(5, 1)), Direction.DOWN),
            Arrow(3, listOf(GridPoint(1, 0), GridPoint(2, 0)), Direction.RIGHT),
            Arrow(4, listOf(GridPoint(4, 0), GridPoint(3, 0)), Direction.LEFT),
            Arrow(5, listOf(GridPoint(1, 1), GridPoint(1, 2)), Direction.DOWN),
            Arrow(6, listOf(GridPoint(4, 1), GridPoint(4, 2)), Direction.DOWN),
            Arrow(7, listOf(GridPoint(2, 1), GridPoint(2, 2)), Direction.DOWN),
            Arrow(8, listOf(GridPoint(3, 1), GridPoint(3, 2)), Direction.DOWN),
            Arrow(9, listOf(GridPoint(0, 3), GridPoint(0, 4), GridPoint(0, 5)), Direction.DOWN),
            Arrow(10, listOf(GridPoint(5, 3), GridPoint(5, 4), GridPoint(5, 5)), Direction.DOWN),
            Arrow(11, listOf(GridPoint(1, 4), GridPoint(2, 4)), Direction.RIGHT),
            Arrow(12, listOf(GridPoint(4, 4), GridPoint(3, 4)), Direction.LEFT),
            Arrow(13, listOf(GridPoint(2, 3), GridPoint(1, 3)), Direction.LEFT),
            Arrow(14, listOf(GridPoint(3, 3), GridPoint(4, 3)), Direction.RIGHT),
            Arrow(15, listOf(GridPoint(2, 5), GridPoint(1, 5)), Direction.LEFT),
            Arrow(16, listOf(GridPoint(3, 5), GridPoint(4, 5)), Direction.RIGHT)
        )
        return LevelConfig(
            levelNumber = 9,
            title = "Castle Gate",
            subtitle = "Breach the battlements",
            gridWidth = 6,
            gridHeight = 6,
            difficulty = GameDifficulty.NORMAL,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.EmeraldForest
        )
    }

    // Level 10: Maze Runner (6x6) - matching aa1.webp & video
    private fun createLevel10(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(1, 0), GridPoint(2, 0), GridPoint(3, 0), GridPoint(4, 0), GridPoint(5, 0)), Direction.RIGHT),
            Arrow(2, listOf(GridPoint(0, 0), GridPoint(0, 1), GridPoint(0, 2)), Direction.DOWN),
            Arrow(3, listOf(GridPoint(5, 1), GridPoint(5, 2), GridPoint(5, 3)), Direction.DOWN),
            Arrow(4, listOf(GridPoint(1, 1), GridPoint(2, 1)), Direction.RIGHT),
            Arrow(5, listOf(GridPoint(4, 1), GridPoint(3, 1)), Direction.LEFT),
            Arrow(6, listOf(GridPoint(1, 2), GridPoint(1, 3)), Direction.DOWN),
            Arrow(7, listOf(GridPoint(4, 2), GridPoint(4, 3)), Direction.DOWN),
            Arrow(8, listOf(GridPoint(2, 2), GridPoint(3, 2)), Direction.RIGHT),
            Arrow(9, listOf(GridPoint(2, 3), GridPoint(3, 3)), Direction.RIGHT),
            Arrow(10, listOf(GridPoint(0, 4), GridPoint(1, 4), GridPoint(2, 4)), Direction.RIGHT),
            Arrow(11, listOf(GridPoint(5, 4), GridPoint(4, 4), GridPoint(3, 4)), Direction.LEFT),
            Arrow(12, listOf(GridPoint(0, 5), GridPoint(1, 5)), Direction.RIGHT),
            Arrow(13, listOf(GridPoint(2, 5), GridPoint(3, 5)), Direction.RIGHT),
            Arrow(14, listOf(GridPoint(5, 5), GridPoint(4, 5)), Direction.LEFT),
            Arrow(15, listOf(GridPoint(5, 0), GridPoint(5, 0)), Direction.UP),
            Arrow(16, listOf(GridPoint(0, 3), GridPoint(0, 3)), Direction.LEFT)
        )
        return LevelConfig(
            levelNumber = 10,
            title = "Maze Runner",
            subtitle = "Navigate the tangled labyrinth",
            gridWidth = 6,
            gridHeight = 6,
            difficulty = GameDifficulty.HARD,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.ClassicIndigo
        )
    }

    // Level 11: Star Cluster (7x7)
    private fun createLevel11(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(3, 0), GridPoint(3, 0)), Direction.UP),
            Arrow(2, listOf(GridPoint(3, 6), GridPoint(3, 6)), Direction.DOWN),
            Arrow(3, listOf(GridPoint(0, 3), GridPoint(0, 3)), Direction.LEFT),
            Arrow(4, listOf(GridPoint(6, 3), GridPoint(6, 3)), Direction.RIGHT),
            Arrow(5, listOf(GridPoint(2, 1), GridPoint(1, 0)), Direction.UP),
            Arrow(6, listOf(GridPoint(4, 1), GridPoint(5, 0)), Direction.UP),
            Arrow(7, listOf(GridPoint(1, 2), GridPoint(0, 1)), Direction.LEFT),
            Arrow(8, listOf(GridPoint(5, 2), GridPoint(6, 1)), Direction.RIGHT),
            Arrow(9, listOf(GridPoint(1, 4), GridPoint(0, 5)), Direction.LEFT),
            Arrow(10, listOf(GridPoint(5, 4), GridPoint(6, 5)), Direction.RIGHT),
            Arrow(11, listOf(GridPoint(2, 5), GridPoint(1, 6)), Direction.DOWN),
            Arrow(12, listOf(GridPoint(4, 5), GridPoint(5, 6)), Direction.DOWN),
            Arrow(13, listOf(GridPoint(2, 2), GridPoint(3, 1), GridPoint(4, 2)), Direction.RIGHT),
            Arrow(14, listOf(GridPoint(4, 3), GridPoint(5, 3)), Direction.RIGHT),
            Arrow(15, listOf(GridPoint(2, 3), GridPoint(1, 3)), Direction.LEFT),
            Arrow(16, listOf(GridPoint(2, 4), GridPoint(3, 5), GridPoint(4, 4)), Direction.RIGHT),
            Arrow(17, listOf(GridPoint(3, 2), GridPoint(3, 3)), Direction.DOWN),
            Arrow(18, listOf(GridPoint(3, 4), GridPoint(3, 3)), Direction.UP)
        )
        return LevelConfig(
            levelNumber = 11,
            title = "Star Cluster",
            subtitle = "Radiating cosmic points",
            gridWidth = 7,
            gridHeight = 7,
            difficulty = GameDifficulty.HARD,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.CyberViolet
        )
    }

    // Level 12: Hourglass (7x7)
    private fun createLevel12(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(0, 0), GridPoint(1, 0), GridPoint(2, 0), GridPoint(3, 0), GridPoint(4, 0), GridPoint(5, 0), GridPoint(6, 0)), Direction.RIGHT),
            Arrow(2, listOf(GridPoint(6, 6), GridPoint(5, 6), GridPoint(4, 6), GridPoint(3, 6), GridPoint(2, 6), GridPoint(1, 6), GridPoint(0, 6)), Direction.LEFT),
            Arrow(3, listOf(GridPoint(1, 1), GridPoint(0, 1)), Direction.LEFT),
            Arrow(4, listOf(GridPoint(5, 1), GridPoint(6, 1)), Direction.RIGHT),
            Arrow(5, listOf(GridPoint(1, 5), GridPoint(0, 5)), Direction.LEFT),
            Arrow(6, listOf(GridPoint(5, 5), GridPoint(6, 5)), Direction.RIGHT),
            Arrow(7, listOf(GridPoint(2, 1), GridPoint(3, 1), GridPoint(4, 1)), Direction.RIGHT),
            Arrow(8, listOf(GridPoint(4, 5), GridPoint(3, 5), GridPoint(2, 5)), Direction.LEFT),
            Arrow(9, listOf(GridPoint(2, 2), GridPoint(1, 2)), Direction.LEFT),
            Arrow(10, listOf(GridPoint(4, 2), GridPoint(5, 2)), Direction.RIGHT),
            Arrow(11, listOf(GridPoint(2, 4), GridPoint(1, 4)), Direction.LEFT),
            Arrow(12, listOf(GridPoint(4, 4), GridPoint(5, 4)), Direction.RIGHT),
            Arrow(13, listOf(GridPoint(3, 2), GridPoint(3, 3)), Direction.DOWN),
            Arrow(14, listOf(GridPoint(3, 4), GridPoint(3, 3)), Direction.UP),
            Arrow(15, listOf(GridPoint(2, 3), GridPoint(1, 3)), Direction.LEFT),
            Arrow(16, listOf(GridPoint(4, 3), GridPoint(5, 3)), Direction.RIGHT),
            Arrow(17, listOf(GridPoint(0, 2), GridPoint(0, 3), GridPoint(0, 4)), Direction.DOWN),
            Arrow(18, listOf(GridPoint(6, 2), GridPoint(6, 3), GridPoint(6, 4)), Direction.DOWN)
        )
        return LevelConfig(
            levelNumber = 12,
            title = "Hourglass",
            subtitle = "Time is running fast",
            gridWidth = 7,
            gridHeight = 7,
            difficulty = GameDifficulty.HARD,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.ObsidianGold
        )
    }

    // Level 13: Fish Tail (7x7)
    private fun createLevel13(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(0, 3), GridPoint(1, 2), GridPoint(2, 1), GridPoint(3, 1)), Direction.RIGHT),
            Arrow(2, listOf(GridPoint(0, 3), GridPoint(1, 4), GridPoint(2, 5), GridPoint(3, 5)), Direction.RIGHT),
            Arrow(3, listOf(GridPoint(4, 1), GridPoint(5, 2), GridPoint(6, 3)), Direction.RIGHT),
            Arrow(4, listOf(GridPoint(4, 5), GridPoint(5, 4), GridPoint(6, 3)), Direction.RIGHT),
            Arrow(5, listOf(GridPoint(6, 1), GridPoint(5, 0)), Direction.UP),
            Arrow(6, listOf(GridPoint(6, 5), GridPoint(5, 6)), Direction.DOWN),
            Arrow(7, listOf(GridPoint(2, 2), GridPoint(3, 2), GridPoint(4, 2)), Direction.RIGHT),
            Arrow(8, listOf(GridPoint(2, 4), GridPoint(3, 4), GridPoint(4, 4)), Direction.RIGHT),
            Arrow(9, listOf(GridPoint(1, 3), GridPoint(2, 3)), Direction.RIGHT),
            Arrow(10, listOf(GridPoint(3, 3), GridPoint(4, 3)), Direction.RIGHT),
            Arrow(11, listOf(GridPoint(5, 3), GridPoint(6, 3)), Direction.RIGHT),
            Arrow(12, listOf(GridPoint(3, 0), GridPoint(2, 0), GridPoint(1, 0)), Direction.LEFT),
            Arrow(13, listOf(GridPoint(3, 6), GridPoint(2, 6), GridPoint(1, 6)), Direction.LEFT),
            Arrow(14, listOf(GridPoint(0, 1), GridPoint(0, 2)), Direction.DOWN),
            Arrow(15, listOf(GridPoint(0, 5), GridPoint(0, 4)), Direction.UP),
            Arrow(16, listOf(GridPoint(6, 2), GridPoint(6, 1)), Direction.UP),
            Arrow(17, listOf(GridPoint(6, 4), GridPoint(6, 5)), Direction.DOWN)
        )
        return LevelConfig(
            levelNumber = 13,
            title = "Fish Tail",
            subtitle = "Streamlined aquatic flow",
            gridWidth = 7,
            gridHeight = 7,
            difficulty = GameDifficulty.HARD,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.OceanCyan
        )
    }

    // Level 14: Double Spiral (7x7)
    private fun createLevel14(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(0, 0), GridPoint(1, 0), GridPoint(2, 0)), Direction.RIGHT),
            Arrow(2, listOf(GridPoint(3, 0), GridPoint(4, 0), GridPoint(5, 0), GridPoint(6, 0)), Direction.RIGHT),
            Arrow(3, listOf(GridPoint(6, 1), GridPoint(6, 2), GridPoint(6, 3)), Direction.DOWN),
            Arrow(4, listOf(GridPoint(6, 4), GridPoint(6, 5), GridPoint(6, 6)), Direction.DOWN),
            Arrow(5, listOf(GridPoint(5, 6), GridPoint(4, 6), GridPoint(3, 6)), Direction.LEFT),
            Arrow(6, listOf(GridPoint(2, 6), GridPoint(1, 6), GridPoint(0, 6)), Direction.LEFT),
            Arrow(7, listOf(GridPoint(0, 5), GridPoint(0, 4), GridPoint(0, 3)), Direction.UP),
            Arrow(8, listOf(GridPoint(0, 2), GridPoint(0, 1)), Direction.UP),
            Arrow(9, listOf(GridPoint(1, 1), GridPoint(2, 1)), Direction.RIGHT),
            Arrow(10, listOf(GridPoint(3, 1), GridPoint(4, 1), GridPoint(5, 1)), Direction.RIGHT),
            Arrow(11, listOf(GridPoint(5, 2), GridPoint(5, 3), GridPoint(5, 4), GridPoint(5, 5)), Direction.DOWN),
            Arrow(12, listOf(GridPoint(4, 5), GridPoint(3, 5), GridPoint(2, 5), GridPoint(1, 5)), Direction.LEFT),
            Arrow(13, listOf(GridPoint(1, 4), GridPoint(1, 3), GridPoint(1, 2)), Direction.UP),
            Arrow(14, listOf(GridPoint(2, 2), GridPoint(3, 2), GridPoint(4, 2)), Direction.RIGHT),
            Arrow(15, listOf(GridPoint(4, 3), GridPoint(4, 4)), Direction.DOWN),
            Arrow(16, listOf(GridPoint(3, 4), GridPoint(2, 4)), Direction.LEFT),
            Arrow(17, listOf(GridPoint(2, 3), GridPoint(3, 3)), Direction.RIGHT)
        )
        return LevelConfig(
            levelNumber = 14,
            title = "Double Spiral",
            subtitle = "Twin whirlpools intertwined",
            gridWidth = 7,
            gridHeight = 7,
            difficulty = GameDifficulty.HARD,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.CyberViolet
        )
    }

    // Level 15: Leaf Veins (7x7) - matching aa5.webp style
    private fun createLevel15(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(0, 1), GridPoint(0, 0)), Direction.UP),
            Arrow(2, listOf(GridPoint(1, 0), GridPoint(2, 0)), Direction.RIGHT),
            Arrow(3, listOf(GridPoint(3, 0), GridPoint(4, 0), GridPoint(5, 1)), Direction.RIGHT),
            Arrow(4, listOf(GridPoint(6, 2), GridPoint(6, 3)), Direction.DOWN),
            Arrow(5, listOf(GridPoint(6, 4), GridPoint(5, 5)), Direction.DOWN),
            Arrow(6, listOf(GridPoint(4, 6), GridPoint(3, 6)), Direction.LEFT),
            Arrow(7, listOf(GridPoint(2, 6), GridPoint(1, 5)), Direction.LEFT),
            Arrow(8, listOf(GridPoint(0, 4), GridPoint(0, 3), GridPoint(0, 2)), Direction.UP),
            Arrow(9, listOf(GridPoint(1, 1), GridPoint(2, 1), GridPoint(3, 1)), Direction.RIGHT),
            Arrow(10, listOf(GridPoint(4, 1), GridPoint(5, 2)), Direction.RIGHT),
            Arrow(11, listOf(GridPoint(5, 3), GridPoint(5, 4)), Direction.DOWN),
            Arrow(12, listOf(GridPoint(4, 5), GridPoint(3, 5)), Direction.LEFT),
            Arrow(13, listOf(GridPoint(2, 5), GridPoint(1, 4)), Direction.LEFT),
            Arrow(14, listOf(GridPoint(1, 3), GridPoint(1, 2)), Direction.UP),
            Arrow(15, listOf(GridPoint(2, 2), GridPoint(3, 2)), Direction.RIGHT),
            Arrow(16, listOf(GridPoint(4, 2), GridPoint(4, 3)), Direction.DOWN),
            Arrow(17, listOf(GridPoint(4, 4), GridPoint(3, 4)), Direction.LEFT),
            Arrow(18, listOf(GridPoint(2, 4), GridPoint(2, 3)), Direction.UP),
            Arrow(19, listOf(GridPoint(3, 3), GridPoint(3, 3)), Direction.RIGHT)
        )
        return LevelConfig(
            levelNumber = 15,
            title = "Leaf Veins",
            subtitle = "Natural branching structures",
            gridWidth = 7,
            gridHeight = 7,
            difficulty = GameDifficulty.HARD,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.EmeraldForest
        )
    }

    // Level 16: Fortress Wall (8x8)
    private fun createLevel16(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(0, 0), GridPoint(1, 0), GridPoint(2, 0)), Direction.RIGHT),
            Arrow(2, listOf(GridPoint(3, 0), GridPoint(4, 0)), Direction.RIGHT),
            Arrow(3, listOf(GridPoint(5, 0), GridPoint(6, 0), GridPoint(7, 0)), Direction.RIGHT),
            Arrow(4, listOf(GridPoint(7, 1), GridPoint(7, 2), GridPoint(7, 3)), Direction.DOWN),
            Arrow(5, listOf(GridPoint(7, 4), GridPoint(7, 5), GridPoint(7, 6), GridPoint(7, 7)), Direction.DOWN),
            Arrow(6, listOf(GridPoint(6, 7), GridPoint(5, 7), GridPoint(4, 7)), Direction.LEFT),
            Arrow(7, listOf(GridPoint(3, 7), GridPoint(2, 7), GridPoint(1, 7), GridPoint(0, 7)), Direction.LEFT),
            Arrow(8, listOf(GridPoint(0, 6), GridPoint(0, 5), GridPoint(0, 4)), Direction.UP),
            Arrow(9, listOf(GridPoint(0, 3), GridPoint(0, 2), GridPoint(0, 1)), Direction.UP),
            Arrow(10, listOf(GridPoint(1, 1), GridPoint(2, 1), GridPoint(3, 1)), Direction.RIGHT),
            Arrow(11, listOf(GridPoint(4, 1), GridPoint(5, 1), GridPoint(6, 1)), Direction.RIGHT),
            Arrow(12, listOf(GridPoint(6, 2), GridPoint(6, 3), GridPoint(6, 4)), Direction.DOWN),
            Arrow(13, listOf(GridPoint(6, 5), GridPoint(6, 6)), Direction.DOWN),
            Arrow(14, listOf(GridPoint(5, 6), GridPoint(4, 6), GridPoint(3, 6)), Direction.LEFT),
            Arrow(15, listOf(GridPoint(2, 6), GridPoint(1, 6)), Direction.LEFT),
            Arrow(16, listOf(GridPoint(1, 5), GridPoint(1, 4), GridPoint(1, 3), GridPoint(1, 2)), Direction.UP),
            Arrow(17, listOf(GridPoint(2, 2), GridPoint(3, 2), GridPoint(4, 2), GridPoint(5, 2)), Direction.RIGHT),
            Arrow(18, listOf(GridPoint(5, 3), GridPoint(5, 4), GridPoint(5, 5)), Direction.DOWN),
            Arrow(19, listOf(GridPoint(4, 5), GridPoint(3, 5), GridPoint(2, 5)), Direction.LEFT),
            Arrow(20, listOf(GridPoint(2, 4), GridPoint(2, 3)), Direction.UP),
            Arrow(21, listOf(GridPoint(3, 3), GridPoint(4, 3)), Direction.RIGHT),
            Arrow(22, listOf(GridPoint(4, 4), GridPoint(3, 4)), Direction.LEFT)
        )
        return LevelConfig(
            levelNumber = 16,
            title = "Fortress Wall",
            subtitle = "Three concentric defensive rings",
            gridWidth = 8,
            gridHeight = 8,
            difficulty = GameDifficulty.EXPERT,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.ClassicIndigo
        )
    }

    // Level 17: Butterfly Wings (8x8)
    private fun createLevel17(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(3, 0), GridPoint(2, 0), GridPoint(1, 0), GridPoint(0, 0)), Direction.LEFT),
            Arrow(2, listOf(GridPoint(4, 0), GridPoint(5, 0), GridPoint(6, 0), GridPoint(7, 0)), Direction.RIGHT),
            Arrow(3, listOf(GridPoint(0, 1), GridPoint(0, 2), GridPoint(0, 3)), Direction.DOWN),
            Arrow(4, listOf(GridPoint(7, 1), GridPoint(7, 2), GridPoint(7, 3)), Direction.DOWN),
            Arrow(5, listOf(GridPoint(0, 4), GridPoint(0, 5), GridPoint(0, 6)), Direction.DOWN),
            Arrow(6, listOf(GridPoint(7, 4), GridPoint(7, 5), GridPoint(7, 6)), Direction.DOWN),
            Arrow(7, listOf(GridPoint(0, 7), GridPoint(1, 7), GridPoint(2, 7), GridPoint(3, 7)), Direction.RIGHT),
            Arrow(8, listOf(GridPoint(7, 7), GridPoint(6, 7), GridPoint(5, 7), GridPoint(4, 7)), Direction.LEFT),
            Arrow(9, listOf(GridPoint(1, 1), GridPoint(2, 1), GridPoint(3, 1)), Direction.RIGHT),
            Arrow(10, listOf(GridPoint(6, 1), GridPoint(5, 1), GridPoint(4, 1)), Direction.LEFT),
            Arrow(11, listOf(GridPoint(1, 2), GridPoint(1, 3)), Direction.DOWN),
            Arrow(12, listOf(GridPoint(6, 2), GridPoint(6, 3)), Direction.DOWN),
            Arrow(13, listOf(GridPoint(1, 4), GridPoint(1, 5)), Direction.DOWN),
            Arrow(14, listOf(GridPoint(6, 4), GridPoint(6, 5)), Direction.DOWN),
            Arrow(15, listOf(GridPoint(1, 6), GridPoint(2, 6), GridPoint(3, 6)), Direction.RIGHT),
            Arrow(16, listOf(GridPoint(6, 6), GridPoint(5, 6), GridPoint(4, 6)), Direction.LEFT),
            Arrow(17, listOf(GridPoint(2, 2), GridPoint(3, 2)), Direction.RIGHT),
            Arrow(18, listOf(GridPoint(5, 2), GridPoint(4, 2)), Direction.LEFT),
            Arrow(19, listOf(GridPoint(2, 3), GridPoint(2, 4)), Direction.DOWN),
            Arrow(20, listOf(GridPoint(5, 3), GridPoint(5, 4)), Direction.DOWN),
            Arrow(21, listOf(GridPoint(2, 5), GridPoint(3, 5)), Direction.RIGHT),
            Arrow(22, listOf(GridPoint(5, 5), GridPoint(4, 5)), Direction.LEFT),
            Arrow(23, listOf(GridPoint(3, 3), GridPoint(3, 4)), Direction.DOWN),
            Arrow(24, listOf(GridPoint(4, 3), GridPoint(4, 4)), Direction.DOWN)
        )
        return LevelConfig(
            levelNumber = 17,
            title = "Butterfly Wings",
            subtitle = "Mirrored biological symmetry",
            gridWidth = 8,
            gridHeight = 8,
            difficulty = GameDifficulty.EXPERT,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.SunsetCrimson
        )
    }

    // Level 18: Labyrinth Core (8x8)
    private fun createLevel18(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(1, 0), GridPoint(2, 0), GridPoint(3, 0)), Direction.RIGHT),
            Arrow(2, listOf(GridPoint(4, 0), GridPoint(5, 0), GridPoint(6, 0)), Direction.RIGHT),
            Arrow(3, listOf(GridPoint(7, 0), GridPoint(7, 1), GridPoint(7, 2)), Direction.DOWN),
            Arrow(4, listOf(GridPoint(7, 3), GridPoint(7, 4), GridPoint(7, 5)), Direction.DOWN),
            Arrow(5, listOf(GridPoint(7, 6), GridPoint(7, 7)), Direction.DOWN),
            Arrow(6, listOf(GridPoint(6, 7), GridPoint(5, 7), GridPoint(4, 7)), Direction.LEFT),
            Arrow(7, listOf(GridPoint(3, 7), GridPoint(2, 7), GridPoint(1, 7)), Direction.LEFT),
            Arrow(8, listOf(GridPoint(0, 7), GridPoint(0, 6), GridPoint(0, 5)), Direction.UP),
            Arrow(9, listOf(GridPoint(0, 4), GridPoint(0, 3), GridPoint(0, 2)), Direction.UP),
            Arrow(10, listOf(GridPoint(0, 1), GridPoint(0, 0)), Direction.UP),
            Arrow(11, listOf(GridPoint(1, 1), GridPoint(2, 1), GridPoint(3, 1), GridPoint(4, 1)), Direction.RIGHT),
            Arrow(12, listOf(GridPoint(5, 1), GridPoint(6, 1), GridPoint(6, 2)), Direction.DOWN),
            Arrow(13, listOf(GridPoint(6, 3), GridPoint(6, 4), GridPoint(6, 5), GridPoint(6, 6)), Direction.DOWN),
            Arrow(14, listOf(GridPoint(5, 6), GridPoint(4, 6), GridPoint(3, 6), GridPoint(2, 6), GridPoint(1, 6)), Direction.LEFT),
            Arrow(15, listOf(GridPoint(1, 5), GridPoint(1, 4), GridPoint(1, 3), GridPoint(1, 2)), Direction.UP),
            Arrow(16, listOf(GridPoint(2, 2), GridPoint(3, 2), GridPoint(4, 2), GridPoint(5, 2)), Direction.RIGHT),
            Arrow(17, listOf(GridPoint(5, 3), GridPoint(5, 4), GridPoint(5, 5)), Direction.DOWN),
            Arrow(18, listOf(GridPoint(4, 5), GridPoint(3, 5), GridPoint(2, 5)), Direction.LEFT),
            Arrow(19, listOf(GridPoint(2, 4), GridPoint(2, 3)), Direction.UP),
            Arrow(20, listOf(GridPoint(3, 3), GridPoint(4, 3)), Direction.RIGHT),
            Arrow(21, listOf(GridPoint(4, 4), GridPoint(3, 4)), Direction.LEFT),
            Arrow(22, listOf(GridPoint(3, 3), GridPoint(3, 4)), Direction.DOWN)
        )
        return LevelConfig(
            levelNumber = 18,
            title = "Labyrinth Core",
            subtitle = "Deep interlocking maze channels",
            gridWidth = 8,
            gridHeight = 8,
            difficulty = GameDifficulty.EXPERT,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.OceanCyan
        )
    }

    // Level 19: Snowflake Crown (8x8)
    private fun createLevel19(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(3, 0), GridPoint(4, 0)), Direction.RIGHT),
            Arrow(2, listOf(GridPoint(7, 3), GridPoint(7, 4)), Direction.DOWN),
            Arrow(3, listOf(GridPoint(4, 7), GridPoint(3, 7)), Direction.LEFT),
            Arrow(4, listOf(GridPoint(0, 4), GridPoint(0, 3)), Direction.UP),
            Arrow(5, listOf(GridPoint(1, 1), GridPoint(0, 0)), Direction.UP),
            Arrow(6, listOf(GridPoint(6, 1), GridPoint(7, 0)), Direction.UP),
            Arrow(7, listOf(GridPoint(6, 6), GridPoint(7, 7)), Direction.DOWN),
            Arrow(8, listOf(GridPoint(1, 6), GridPoint(0, 7)), Direction.DOWN),
            Arrow(9, listOf(GridPoint(2, 1), GridPoint(2, 0)), Direction.UP),
            Arrow(10, listOf(GridPoint(5, 1), GridPoint(5, 0)), Direction.UP),
            Arrow(11, listOf(GridPoint(6, 2), GridPoint(7, 2)), Direction.RIGHT),
            Arrow(12, listOf(GridPoint(6, 5), GridPoint(7, 5)), Direction.RIGHT),
            Arrow(13, listOf(GridPoint(5, 6), GridPoint(5, 7)), Direction.DOWN),
            Arrow(14, listOf(GridPoint(2, 6), GridPoint(2, 7)), Direction.DOWN),
            Arrow(15, listOf(GridPoint(1, 5), GridPoint(0, 5)), Direction.LEFT),
            Arrow(16, listOf(GridPoint(1, 2), GridPoint(0, 2)), Direction.LEFT),
            Arrow(17, listOf(GridPoint(2, 2), GridPoint(3, 2), GridPoint(4, 2), GridPoint(5, 2)), Direction.RIGHT),
            Arrow(18, listOf(GridPoint(5, 3), GridPoint(5, 4), GridPoint(5, 5)), Direction.DOWN),
            Arrow(19, listOf(GridPoint(4, 5), GridPoint(3, 5), GridPoint(2, 5)), Direction.LEFT),
            Arrow(20, listOf(GridPoint(2, 4), GridPoint(2, 3)), Direction.UP),
            Arrow(21, listOf(GridPoint(3, 3), GridPoint(4, 3)), Direction.RIGHT),
            Arrow(22, listOf(GridPoint(4, 4), GridPoint(3, 4)), Direction.LEFT),
            Arrow(23, listOf(GridPoint(3, 1), GridPoint(3, 0)), Direction.UP),
            Arrow(24, listOf(GridPoint(4, 1), GridPoint(4, 0)), Direction.UP),
            Arrow(25, listOf(GridPoint(1, 3), GridPoint(0, 3)), Direction.LEFT),
            Arrow(26, listOf(GridPoint(1, 4), GridPoint(0, 4)), Direction.LEFT)
        )
        return LevelConfig(
            levelNumber = 19,
            title = "Snowflake Crown",
            subtitle = "Crystalline precision puzzle",
            gridWidth = 8,
            gridHeight = 8,
            difficulty = GameDifficulty.EXPERT,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.CyberViolet
        )
    }

    // Level 20: Master Matrix (8x8)
    private fun createLevel20(): LevelConfig {
        val arrows = listOf(
            Arrow(1, listOf(GridPoint(0, 0), GridPoint(1, 0), GridPoint(2, 0)), Direction.RIGHT),
            Arrow(2, listOf(GridPoint(3, 0), GridPoint(4, 0)), Direction.RIGHT),
            Arrow(3, listOf(GridPoint(5, 0), GridPoint(6, 0), GridPoint(7, 0)), Direction.RIGHT),
            Arrow(4, listOf(GridPoint(7, 1), GridPoint(7, 2), GridPoint(7, 3)), Direction.DOWN),
            Arrow(5, listOf(GridPoint(7, 4), GridPoint(7, 5), GridPoint(7, 6)), Direction.DOWN),
            Arrow(6, listOf(GridPoint(7, 7), GridPoint(6, 7), GridPoint(5, 7)), Direction.LEFT),
            Arrow(7, listOf(GridPoint(4, 7), GridPoint(3, 7)), Direction.LEFT),
            Arrow(8, listOf(GridPoint(2, 7), GridPoint(1, 7), GridPoint(0, 7)), Direction.LEFT),
            Arrow(9, listOf(GridPoint(0, 6), GridPoint(0, 5), GridPoint(0, 4)), Direction.UP),
            Arrow(10, listOf(GridPoint(0, 3), GridPoint(0, 2), GridPoint(0, 1)), Direction.UP),
            Arrow(11, listOf(GridPoint(1, 1), GridPoint(2, 1), GridPoint(3, 1)), Direction.RIGHT),
            Arrow(12, listOf(GridPoint(4, 1), GridPoint(5, 1), GridPoint(6, 1)), Direction.RIGHT),
            Arrow(13, listOf(GridPoint(6, 2), GridPoint(6, 3), GridPoint(6, 4)), Direction.DOWN),
            Arrow(14, listOf(GridPoint(6, 5), GridPoint(6, 6)), Direction.DOWN),
            Arrow(15, listOf(GridPoint(5, 6), GridPoint(4, 6), GridPoint(3, 6)), Direction.LEFT),
            Arrow(16, listOf(GridPoint(2, 6), GridPoint(1, 6)), Direction.LEFT),
            Arrow(17, listOf(GridPoint(1, 5), GridPoint(1, 4), GridPoint(1, 3)), Direction.UP),
            Arrow(18, listOf(GridPoint(1, 2), GridPoint(2, 2)), Direction.RIGHT),
            Arrow(19, listOf(GridPoint(3, 2), GridPoint(4, 2), GridPoint(5, 2)), Direction.RIGHT),
            Arrow(20, listOf(GridPoint(5, 3), GridPoint(5, 4), GridPoint(5, 5)), Direction.DOWN),
            Arrow(21, listOf(GridPoint(4, 5), GridPoint(3, 5), GridPoint(2, 5)), Direction.LEFT),
            Arrow(22, listOf(GridPoint(2, 4), GridPoint(2, 3)), Direction.UP),
            Arrow(23, listOf(GridPoint(3, 3), GridPoint(4, 3)), Direction.RIGHT),
            Arrow(24, listOf(GridPoint(4, 4), GridPoint(3, 4)), Direction.LEFT),
            Arrow(25, listOf(GridPoint(3, 3), GridPoint(3, 2)), Direction.UP),
            Arrow(26, listOf(GridPoint(4, 4), GridPoint(4, 5)), Direction.DOWN),
            Arrow(27, listOf(GridPoint(2, 3), GridPoint(1, 3)), Direction.LEFT),
            Arrow(28, listOf(GridPoint(5, 4), GridPoint(6, 4)), Direction.RIGHT)
        )
        return LevelConfig(
            levelNumber = 20,
            title = "Master Matrix",
            subtitle = "The ultimate brain test",
            gridWidth = 8,
            gridHeight = 8,
            difficulty = GameDifficulty.EXPERT,
            initialArrows = arrows,
            defaultPalette = PaletteTheme.ObsidianGold
        )
    }
}

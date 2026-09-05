package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.LevelDefinitions
import com.example.model.LevelSolver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context matches Tap Away Arrows`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Tap Away Arrows", appName)
    }

    @Test
    fun `verify all 20 levels are reverse topologically solvable`() {
        val allLevels = LevelDefinitions.levels
        assertEquals(20, allLevels.size)

        allLevels.forEach { level ->
            val isSolvable = LevelSolver.verifySolvability(
                initialArrows = level.initialArrows,
                gridWidth = level.gridWidth,
                gridHeight = level.gridHeight
            )
            assertTrue("Level ${level.levelNumber} (${level.title}) must be 100% solvable", isSolvable)
        }
    }

    @Test
    fun `verify hint system finds free arrow for initial levels`() {
        val level1 = LevelDefinitions.getLevel(1)
        val freeArrow = LevelSolver.findAvailableFreeArrow(
            activeArrows = level1.initialArrows,
            gridWidth = level1.gridWidth,
            gridHeight = level1.gridHeight
        )
        assertNotNull("Level 1 must have at least one free arrow immediately available", freeArrow)
    }
}

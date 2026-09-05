package com.example.model

/**
 * Solvability and collision helper for Tap Away Arrows.
 */
object LevelSolver {

    /**
     * Checks whether an arrow is free to fly off the board without hitting any other active arrow.
     */
    fun isArrowFree(
        arrow: Arrow,
        activeArrows: List<Arrow>,
        gridWidth: Int,
        gridHeight: Int
    ): Boolean {
        val otherArrows = activeArrows.filter { it.id != arrow.id }
        var currX = arrow.head.x + arrow.exitDirection.dx
        var currY = arrow.head.y + arrow.exitDirection.dy

        while (currX in 0 until gridWidth && currY in 0 until gridHeight) {
            val pointX = currX
            val pointY = currY
            val blocker = otherArrows.find { it.occupies(pointX, pointY) }
            if (blocker != null) {
                return false
            }
            currX += arrow.exitDirection.dx
            currY += arrow.exitDirection.dy
        }
        return true
    }

    /**
     * Finds the blocker arrow if blocked, or null if free.
     */
    fun findFirstBlocker(
        arrow: Arrow,
        activeArrows: List<Arrow>,
        gridWidth: Int,
        gridHeight: Int
    ): Arrow? {
        val otherArrows = activeArrows.filter { it.id != arrow.id }
        var currX = arrow.head.x + arrow.exitDirection.dx
        var currY = arrow.head.y + arrow.exitDirection.dy

        while (currX in 0 until gridWidth && currY in 0 until gridHeight) {
            val pointX = currX
            val pointY = currY
            val blocker = otherArrows.find { it.occupies(pointX, pointY) }
            if (blocker != null) {
                return blocker
            }
            currX += arrow.exitDirection.dx
            currY += arrow.exitDirection.dy
        }
        return null
    }

    /**
     * Finds any available free arrow (for Hint feature).
     */
    fun findAvailableFreeArrow(
        activeArrows: List<Arrow>,
        gridWidth: Int,
        gridHeight: Int
    ): Arrow? {
        return activeArrows.firstOrNull { isArrowFree(it, activeArrows, gridWidth, gridHeight) }
    }

    /**
     * Mathematically verifies that a level has 100% reverse topological solvability.
     */
    fun verifySolvability(
        initialArrows: List<Arrow>,
        gridWidth: Int,
        gridHeight: Int
    ): Boolean {
        val remaining = initialArrows.toMutableList()
        while (remaining.isNotEmpty()) {
            val freeArrow = remaining.firstOrNull { isArrowFree(it, remaining, gridWidth, gridHeight) }
                ?: return false
            remaining.remove(freeArrow)
        }
        return true
    }
}

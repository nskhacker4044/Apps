package com.example.data

import com.example.data.dao.LevelDao
import com.example.data.entity.GameStats
import com.example.data.entity.LevelProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class GameRepository(private val levelDao: LevelDao) {

    val allLevelProgress: Flow<List<LevelProgress>> = levelDao.getAllLevelProgress()
    val gameStats: Flow<GameStats?> = levelDao.getGameStats()

    suspend fun initializeDefaultsIfNeeded(totalLevels: Int = 20) {
        val existing = levelDao.getAllLevelProgress().firstOrNull()
        if (existing.isNullOrEmpty()) {
            val initialList = (1..totalLevels).map { num ->
                LevelProgress(
                    levelNumber = num,
                    isUnlocked = (num == 1), // First level is unlocked
                    starsEarned = 0,
                    highScore = 0,
                    bestTimeSeconds = 0
                )
            }
            levelDao.insertAllLevels(initialList)
        }

        val stats = levelDao.getGameStats().firstOrNull()
        if (stats == null) {
            levelDao.insertOrUpdateStats(GameStats())
        }
    }

    suspend fun saveLevelCompletion(
        levelNumber: Int,
        stars: Int,
        score: Int,
        timeSeconds: Int,
        nextLevelNumber: Int?
    ) {
        val current = levelDao.getLevelProgress(levelNumber).firstOrNull()
        val bestStars = maxOf(current?.starsEarned ?: 0, stars)
        val bestScore = maxOf(current?.highScore ?: 0, score)
        val bestTime = if ((current?.bestTimeSeconds ?: 0) == 0) timeSeconds else minOf(current!!.bestTimeSeconds, timeSeconds)

        levelDao.insertOrUpdateLevel(
            LevelProgress(
                levelNumber = levelNumber,
                isUnlocked = true,
                starsEarned = bestStars,
                highScore = bestScore,
                bestTimeSeconds = bestTime,
                completedAt = System.currentTimeMillis()
            )
        )

        // Unlock next level if present
        if (nextLevelNumber != null) {
            val nextProg = levelDao.getLevelProgress(nextLevelNumber).firstOrNull()
            if (nextProg == null || !nextProg.isUnlocked) {
                levelDao.insertOrUpdateLevel(
                    LevelProgress(
                        levelNumber = nextLevelNumber,
                        isUnlocked = true,
                        starsEarned = nextProg?.starsEarned ?: 0,
                        highScore = nextProg?.highScore ?: 0,
                        bestTimeSeconds = nextProg?.bestTimeSeconds ?: 0
                    )
                )
            }
        }
    }

    suspend fun updateStats(stats: GameStats) {
        levelDao.insertOrUpdateStats(stats)
    }

    suspend fun addCoins(amount: Int) {
        val current = levelDao.getGameStats().firstOrNull() ?: GameStats()
        val newCoins = (current.totalCoins + amount).coerceAtLeast(0)
        levelDao.insertOrUpdateStats(current.copy(totalCoins = newCoins))
    }

    suspend fun useHint(): Boolean {
        val current = levelDao.getGameStats().firstOrNull() ?: GameStats()
        if (current.hintsRemaining > 0) {
            levelDao.insertOrUpdateStats(current.copy(hintsRemaining = current.hintsRemaining - 1))
            return true
        }
        return false
    }

    suspend fun addHints(count: Int) {
        val current = levelDao.getGameStats().firstOrNull() ?: GameStats()
        levelDao.insertOrUpdateStats(current.copy(hintsRemaining = current.hintsRemaining + count))
    }

    suspend fun resetAll(totalLevels: Int = 20) {
        levelDao.clearLevelProgress()
        levelDao.clearGameStats()
        initializeDefaultsIfNeeded(totalLevels)
    }
}

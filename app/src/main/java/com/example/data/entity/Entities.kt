package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "level_progress")
data class LevelProgress(
    @PrimaryKey val levelNumber: Int,
    val isUnlocked: Boolean = false,
    val starsEarned: Int = 0,
    val highScore: Int = 0,
    val bestTimeSeconds: Int = 0,
    val completedAt: Long = 0L
)

@Entity(tableName = "game_stats")
data class GameStats(
    @PrimaryKey val id: Int = 1,
    val totalCoins: Int = 0,
    val hintsRemaining: Int = 5,
    val selectedPaletteId: String = "classic_indigo",
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val sfxVolume: Float = 0.8f
)

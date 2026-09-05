package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.GameStats
import com.example.data.entity.LevelProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelDao {
    @Query("SELECT * FROM level_progress ORDER BY levelNumber ASC")
    fun getAllLevelProgress(): Flow<List<LevelProgress>>

    @Query("SELECT * FROM level_progress WHERE levelNumber = :levelNumber")
    fun getLevelProgress(levelNumber: Int): Flow<LevelProgress?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLevel(progress: LevelProgress)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLevels(progressList: List<LevelProgress>)

    @Query("SELECT * FROM game_stats WHERE id = 1")
    fun getGameStats(): Flow<GameStats?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStats(stats: GameStats)

    @Query("DELETE FROM level_progress")
    suspend fun clearLevelProgress()

    @Query("DELETE FROM game_stats")
    suspend fun clearGameStats()
}

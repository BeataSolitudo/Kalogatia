package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.kalogatia.data.entities.HistoryWorkout
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryWorkoutDao {
    @Upsert
    suspend fun upsertHistoryWorkout(historyWorkout: HistoryWorkout)

    @Delete
    suspend fun deleteHistoryWorkout(historyWorkout: HistoryWorkout)

    @Query("SELECT * FROM historyworkout")
    fun selectAllHistoryWorkout(): Flow<List<HistoryWorkout>>

    @Query("INSERT INTO HistoryWorkout(name, userId) VALUES(:name, :userId)")
    suspend fun insertHistoryWorkout(name: String, userId: Int): Long
}
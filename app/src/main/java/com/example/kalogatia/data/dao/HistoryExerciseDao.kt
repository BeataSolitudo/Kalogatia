package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.kalogatia.data.entities.Exercise
import com.example.kalogatia.data.entities.HistoryExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryExerciseDao {
    @Upsert
    suspend fun upsertHistoryExercise(historyExercise: HistoryExercise)

    @Delete
    suspend fun deleteHistoryExercise(historyExercise: HistoryExercise)

    @Query("SELECT * FROM historyExercise")
    fun selectAllHistoryExercise(): Flow<List<HistoryExercise>>

    @Query("INSERT INTO HistoryExercise(exerciseTypeId, restTime, historyWorkoutId) VALUES(:exerciseType, :restTime, :historyWorkoutId)")
    suspend fun insertExercise(exerciseType: Int, restTime: Int, historyWorkoutId: Int): Long

    @Query("SELECT * FROM HistoryExercise WHERE exerciseTypeId = :exerciseTypeId")
    fun fetchHistoryExercisesByExerciseType(exerciseTypeId: Int): Flow<List<HistoryExercise?>>
}
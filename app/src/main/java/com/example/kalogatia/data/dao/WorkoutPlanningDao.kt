package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.kalogatia.data.entities.WorkoutPlanning
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutPlanningDao {
    @Upsert
    suspend fun upsertWorkoutPlanning(workoutPlanning: WorkoutPlanning)

    @Delete
    suspend fun deleteWorkoutPlanning(workoutPlanning: WorkoutPlanning)

    @Query("SELECT * FROM workoutPlanning")
    fun selectAllWorkoutPlanning(): Flow<List<WorkoutPlanning>>

    @Query("SELECT weekDay FROM workoutplanning WHERE workoutId = :workoutId LIMIT 1")
    fun getWorkoutDay(workoutId: Int): Int
}
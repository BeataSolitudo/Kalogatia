package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.kalogatia.data.entities.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Upsert
    suspend fun upsertWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("SELECT * FROM workout")
    fun selectAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT workoutId FROM workout WHERE name LIKE :workoutName LIMIT 1")
    suspend fun selectWorkoutByName(workoutName: String): Int?
}
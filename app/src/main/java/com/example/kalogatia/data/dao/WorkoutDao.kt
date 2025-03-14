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

    @Query("SELECT workout.name FROM workout INNER JOIN workoutPlanning ON workout.workoutId = workoutPlanning.workoutId WHERE workoutPlanning.weekDay = :day")
    suspend fun selectWorkoutByDay(day: Int): String?

    @Query("SELECT name FROM workout WHERE workoutId = :workoutId")
    fun getWorkoutName(workoutId: Int): String?

    @Query("INSERT INTO workout (name, userId) VALUES (:workoutName, :userId)")
    suspend fun insertWorkout(workoutName: String, userId: Int)

    @Query("UPDATE workout SET name = :workoutName, userId = :userId WHERE workoutId = :workoutId")
    suspend fun updateWorkout(workoutId: Int ,workoutName: String, userId: Int)

    @Query("SELECT workout.* FROM workout LEFT JOIN workoutplanning ON workout.workoutId = workoutplanning.workoutId WHERE workoutplanning.workoutId IS NULL")
    fun selectIncompleteWorkouts(): Flow<List<Workout>?>
}
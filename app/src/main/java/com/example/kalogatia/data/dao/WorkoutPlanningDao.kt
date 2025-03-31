package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.kalogatia.data.entities.WorkoutPlanning
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutPlanningDao {
    @Upsert
    suspend fun upsertWorkoutPlanning(workoutPlanning: WorkoutPlanning)
//
//    @Delete
//    suspend fun deleteWorkoutPlanning(workoutPlanning: WorkoutPlanning)

    @Query("SELECT * FROM workoutPlanning")
    fun selectAllWorkoutPlanning(): Flow<List<WorkoutPlanning>>

    @Query("SELECT weekDay FROM workoutplanning WHERE workoutId = :workoutId LIMIT 1")
    fun getWorkoutDay(workoutId: Int): Flow<Int?>

    @Query("SELECT * FROM workoutplanning WHERE workoutId = :workoutId")
    fun fetchWorkoutPlanning(workoutId: Int): Flow<WorkoutPlanning?>

    @Query("INSERT INTO workoutPlanning(workoutId, userId, weekDay) VALUES (:workoutId, :userId, :weekDay)")
    suspend fun insertWorkoutPlanning(workoutId: Int, userId: Int, weekDay: Int)

    @Query("UPDATE workoutplanning SET weekDay = :newWeekDay WHERE workoutId = :workoutId AND weekDay = :weekDay")
    suspend fun updateWorkoutPlanningWeekDay(workoutId: Int, weekDay: Int, newWeekDay: Int)

    @Query("DELETE FROM workoutplanning WHERE workoutId = :workoutId AND weekDay = :weekDay")
    suspend fun deleteWorkoutPlanning(workoutId: Int, weekDay: Int)

    @Query("DELETE FROM workoutplanning WHERE workoutId = :workoutId")
    suspend fun deleteWorkoutPlanning(workoutId: Int)

    @Query("SELECT weekDay FROM workoutplanning WHERE workoutId = :workoutId")
    fun fetchWorkoutDays(workoutId: Int): Flow<List<Int>?>

    @Query("DELETE FROM workoutplanning WHERE workoutId = :workoutId AND weekDay IN (:weekDays)")
    suspend fun deleteWorkoutPlanning(workoutId: Int, weekDays: List<Int>)

}
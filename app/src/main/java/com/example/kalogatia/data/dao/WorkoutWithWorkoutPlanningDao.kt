package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.kalogatia.data.relations.WorkoutWithWorkoutPlanning
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutWithWorkoutPlanningDao {
    @Query("SELECT * FROM workoutplanning INNER JOIN workout ON workoutPlanning.workoutId = workout.workoutId")
    fun getAllWorkoutsWithWorkoutPlanning(): Flow<List<WorkoutWithWorkoutPlanning>>
}
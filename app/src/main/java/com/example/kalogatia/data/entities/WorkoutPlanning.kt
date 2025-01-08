package com.example.kalogatia.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class WorkoutPlanning (
    val workoutId: Int,
    val userId: Int,
    val weekDay: Int,
    @PrimaryKey(autoGenerate = true)
    val workoutPlanningId: Int? = null
)
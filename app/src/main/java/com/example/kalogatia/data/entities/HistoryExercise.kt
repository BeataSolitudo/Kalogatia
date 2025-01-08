package com.example.kalogatia.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class HistoryExercise (
    val exerciseTypeId: Int,
    val restTime: Double,
    val createdAt: Long,
    val historyWorkoutId: Int,
    @PrimaryKey(autoGenerate = true)
    val historyExerciseId: Int
)
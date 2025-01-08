package com.example.kalogatia.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class HistoryWorkout (
    val name: String,
    val myDate: Long,
    val createdAt: Long,
    val userId: Int,
    @PrimaryKey(autoGenerate = true)
    val historyWorkoutId: Int
)
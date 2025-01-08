package com.example.kalogatia.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class Exercise (
    val exerciseTypeId: Int,
    val restTime: Double,
    val createdAt: Long,
    val workoutId: Int,
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int? = null
)
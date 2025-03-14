package com.example.kalogatia.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise (
    val exerciseTypeId: Int,
    val restTime: Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: String? = null,
    val workoutId: Int,
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int? = null
)
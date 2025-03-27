package com.example.kalogatia.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class HistoryExercise (
    val exerciseTypeId: Int,
    val restTime: Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: String? = null,
    val historyWorkoutId: Int,
    @PrimaryKey(autoGenerate = true)
    val historyExerciseId: Int? = null
)
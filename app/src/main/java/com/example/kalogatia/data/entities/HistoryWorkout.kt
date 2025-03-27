package com.example.kalogatia.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class HistoryWorkout (
    val name: String,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: String? = null,
    val userId: Int,
    @PrimaryKey(autoGenerate = true)
    val historyWorkoutId: Int? = null
)
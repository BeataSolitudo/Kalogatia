package com.example.kalogatia.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class Workout (
    val name: String,
    val createdAt: Long,
    val userId: Int,
    @PrimaryKey(autoGenerate = true)
    val workoutId: Int? = null
)
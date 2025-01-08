package com.example.kalogatia.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class HistorySet (
    val setNumber: Int,
    val weight: Double,
    val repetition: Int,
    val createdAt: Long,
    val historyExerciseId: Int,
    @PrimaryKey(autoGenerate = true)
    val historySetId: Int
)
package com.example.kalogatia.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Set (
    val setNumber: Int,
    val weight: Double,
    val repetition: Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: String? = null,
    val exerciseId: Int,
    @PrimaryKey(autoGenerate = true)
    val setId: Int? = null
)
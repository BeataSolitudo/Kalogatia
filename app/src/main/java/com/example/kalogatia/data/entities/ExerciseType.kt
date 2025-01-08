package com.example.kalogatia.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExerciseType (
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val exerciseTypeId: Int? = null
)
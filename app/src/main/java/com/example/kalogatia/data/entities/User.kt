package com.example.kalogatia.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    val username: String,
    val mail: String,
    val password: String,
    @PrimaryKey(autoGenerate = true)
    val userId: Int? = null
)
package com.example.kalogatia.data

data class Workout(
    val workoutId: Int,
    val workoutName: String,
    val exercises: List<Exercise>,
    val workoutDay: String,
)

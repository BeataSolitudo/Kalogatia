package com.example.kalogatia.data

data class Progress(
    val workoutId: Int,
    val exerciseId: Int,
    val date: String,
    val weight: List<Double>,
    val sets: Int,
    val reps: List<Int>,
    val restTime: Int,
)

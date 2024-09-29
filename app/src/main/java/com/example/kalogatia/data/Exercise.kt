package com.example.kalogatia.data

data class Exercise(
    val exerciseId: Int,
    val exerciseName: String,
    val restTime: Int,
    val sets: List<Int>,
    val weight: List<Double>,
)

package com.example.kalogatia.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.kalogatia.data.entities.Exercise
import com.example.kalogatia.data.entities.Workout

data class WorkoutWithExercises (
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutId"
    )
    val exercises: List<Exercise>
)
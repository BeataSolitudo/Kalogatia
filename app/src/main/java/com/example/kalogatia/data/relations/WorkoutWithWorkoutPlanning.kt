package com.example.kalogatia.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.kalogatia.data.entities.Workout
import com.example.kalogatia.data.entities.WorkoutPlanning

data class WorkoutWithWorkoutPlanning (
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutId"
    )
    val workoutPlanning: List<WorkoutPlanning>
)
package com.example.kalogatia.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.kalogatia.data.entities.Exercise

data class ExerciseTypeWithExercises (
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "exerciseTypeId",
        entityColumn = "exerciseTypeId"
    )
    val exercises: List<Exercise>
)
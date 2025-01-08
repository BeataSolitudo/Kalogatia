package com.example.kalogatia.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.kalogatia.data.entities.Exercise
import com.example.kalogatia.data.entities.Set

data class ExerciseWithSets (
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseId"
    )
    val sets: List<Set>
)
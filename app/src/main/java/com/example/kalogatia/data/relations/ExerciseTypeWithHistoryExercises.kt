package com.example.kalogatia.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.kalogatia.data.entities.ExerciseType
import com.example.kalogatia.data.entities.HistoryExercise

data class ExerciseTypeWithHistoryExercises (
    @Embedded val exerciseType: ExerciseType,
    @Relation(
        parentColumn = "exerciseTypeId",
        entityColumn = "exerciseTypeId"
    )
    val historyExercises: List<HistoryExercise>
)
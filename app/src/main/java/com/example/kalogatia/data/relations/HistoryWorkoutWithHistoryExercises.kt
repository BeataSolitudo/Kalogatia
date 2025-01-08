package com.example.kalogatia.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.kalogatia.data.entities.HistoryExercise
import com.example.kalogatia.data.entities.HistoryWorkout

data class HistoryWorkoutWithHistoryExercises (
    @Embedded val historyWorkout: HistoryWorkout,
    @Relation(
        parentColumn = "historyWorkoutId",
        entityColumn = "historyWorkoutId"
    )
    val historyExercises: List<HistoryExercise>
)
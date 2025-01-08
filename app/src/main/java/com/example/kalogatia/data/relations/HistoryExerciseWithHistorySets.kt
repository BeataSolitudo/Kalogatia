package com.example.kalogatia.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.kalogatia.data.entities.HistoryExercise
import com.example.kalogatia.data.entities.HistorySet

data class HistoryExerciseWithHistorySets (
    @Embedded val historyExercise: HistoryExercise,
    @Relation(
        parentColumn = "historyExerciseId",
        entityColumn = "historyExerciseId"
    )
    val historySets: List<HistorySet>
)
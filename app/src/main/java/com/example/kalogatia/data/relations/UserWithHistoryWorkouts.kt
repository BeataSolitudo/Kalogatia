package com.example.kalogatia.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.kalogatia.data.entities.HistoryWorkout
import com.example.kalogatia.data.entities.User

data class UserWithHistoryWorkouts (
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val historyWorkouts: List<HistoryWorkout>
)
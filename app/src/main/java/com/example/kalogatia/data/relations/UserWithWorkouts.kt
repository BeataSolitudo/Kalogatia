package com.example.kalogatia.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.kalogatia.data.entities.User
import com.example.kalogatia.data.entities.Workout

data class UserWithWorkouts (
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val workouts: List<Workout>
)
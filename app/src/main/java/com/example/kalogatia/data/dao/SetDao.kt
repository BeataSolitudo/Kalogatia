package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import com.example.kalogatia.data.entities.Set

@Dao
interface SetDao {
    @Upsert
    suspend fun upsertSet(set: Set)

    @Delete
    suspend fun deleteSet(set: Set)

    @Query("SELECT * FROM 'set'")
    fun selectAllSets(): Flow<List<Set>>

    @Query("SELECT MAX(setNumber) FROM 'set' WHERE exerciseId = :exerciseId")
    suspend fun getMaxSetNumberExerciseId(exerciseId: Int): Int?

    // fix: zjistiti díru, když smaže cvik a přerovnat
    suspend fun autoNumbering(exerciseId: Int): Int {
        val maxNumber = getMaxSetNumberExerciseId(exerciseId)

        return if (maxNumber != null) {
            maxNumber + 1
        } else {
            1
        }
    }
}

package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.kalogatia.data.entities.Set
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT COUNT(*) FROM `Set` WHERE exerciseId = :exerciseId")
    suspend fun countSetsByExerciseId(exerciseId: Int): Int

    @Query("SELECT weight FROM `Set` WHERE exerciseId = :exerciseId")
    fun fetchWeights(exerciseId: Int): Flow<List<Double>>

    @Query("SELECT MAX(weight) FROM `Set` WHERE exerciseId = :exerciseId")
    suspend fun getMaxWeightByExerciseId(exerciseId: Int): Double?

}

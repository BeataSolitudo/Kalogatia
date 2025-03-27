package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.kalogatia.data.entities.HistorySet
import com.example.kalogatia.data.entities.Set
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorySetDao {
    @Upsert
    suspend fun upsertHistorySet(historySet: HistorySet)

    @Delete
    suspend fun deleteHistorySet(historySet: HistorySet)

    @Query("SELECT * FROM historySet")
    fun selectAllHistorySet(): Flow<List<HistorySet>>

    @Query("INSERT INTO HistorySet(setNumber, weight, repetition, historyExerciseId) VALUES(:setNumber, :weight, :repetition, :historyExerciseId)")
    suspend fun insertSet(setNumber: Int, weight: Double, repetition: Int, historyExerciseId: Int)

    @Query("SELECT * FROM HistorySet WHERE historyExerciseId IN (:historyExercisesIds)")
    fun fetchHistorySets(historyExercisesIds: List<Int>): Flow<List<HistorySet>>

    @Query("""
    SELECT MAX(historySet.weight) AS max_weight, strftime('%Y', historySet.createdAt) AS year, strftime('%W', historySet.createdAt) AS week_of_year
    FROM historySet 
    INNER JOIN historyExercise 
    ON historySet.historyExerciseId = historyExercise.historyExerciseId 
    WHERE historyExercise.exercisetypeId = :exerciseTypeId 
    GROUP BY year, week_of_year
    """)
    fun fetchSetsByExerciseType(exerciseTypeId: Int): Flow<List<SetsLittleHelper>>

}

data class SetsLittleHelper(
    val max_weight: Double?,
    val year: String?,
    val week_of_year: String?
)

package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.kalogatia.data.entities.ExerciseType
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseTypeDao {
    @Upsert
    suspend fun upsertExerciseType(exerciseType: ExerciseType)

    @Delete
    suspend fun deleteExerciseType(exerciseType: ExerciseType)

    @Query("SELECT * FROM exerciseType")
    fun selectAllExerciseType(): Flow<List<ExerciseType>>

    /*Musí být suspend?*/
    @Query("SELECT exerciseTypeId FROM ExerciseType WHERE name LIKE :exerciseTypeName LIMIT 1")
    suspend fun selectExerciseTypeByName(exerciseTypeName: String): Int?

    @Query("INSERT INTO exerciseType(name) VALUES (:name)")
    suspend fun insertExerciseType(name: String)

    @Query("SELECT name FROM exercisetype WHERE exerciseTypeId = :exerciseTypeId")
    suspend fun fetchExerciseTypeName(exerciseTypeId: Int): String?

    @Query("UPDATE exercisetype SET name = :exerciseTypeName WHERE exerciseTypeId = :exerciseTypeId")
    suspend fun updateExerciseType(exerciseTypeName: String, exerciseTypeId: Int)
}
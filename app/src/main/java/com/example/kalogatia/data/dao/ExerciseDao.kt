package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.kalogatia.data.entities.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Upsert
    suspend fun upsertExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM exercise")
    fun selectAllExercises(): Flow<List<Exercise>>

    @Query("SELECT exerciseTypeId FROM exerciseType WHERE name = :exerciseName LIMIT 1")
    suspend fun getExerciseTypeIdByName(exerciseName: String): Int?

    @Query("SELECT exerciseId FROM exercise WHERE exerciseTypeId = :exerciseTypeId LIMIT 1")
    suspend fun getExerciseIdByTypeId(exerciseTypeId: Int): Int?

    @Transaction
    suspend fun getExerciseIdByName(exerciseName: String): Int? {
        val exerciseTypeId = getExerciseTypeIdByName(exerciseName)
        return exerciseTypeId?.let { getExerciseIdByTypeId(it) }
    }

    @Query("SELECT * FROM exercise WHERE workoutId = :workoutId")
    fun fetchExercisesByWorkoutId(workoutId: Int): Flow<List<Exercise>>

    @Query("INSERT INTO exercise (exerciseTypeId, restTime, workoutId) VALUES (:exerciseTypeId, :restTime, :workoutId)")
    suspend fun insertExercise(exerciseTypeId: Int, restTime: Int, workoutId: Int): Long

    @Query("SELECT * FROM exercise WHERE exerciseId = :exerciseId")
    suspend fun fetchExercise(exerciseId: Int): Exercise

    @Query("UPDATE exercise SET restTime = :restTime WHERE exerciseId = :exerciseId")
    suspend fun updateRestTime(exerciseId: Int, restTime: Int)

    @Query("DELETE FROM exercise WHERE workoutId = :workoutId")
    suspend fun deleteExercise(workoutId: Int)

    @Query("SELECT exerciseId FROM exercise WHERE workoutId = :workoutId")
    fun fetchExerciseIds(workoutId: Int): Flow<List<Int>>

    @Query("DELETE FROM exercise WHERE exerciseId = :exerciseId")
    suspend fun deleteExerciseByExerciseId(exerciseId: Int)
}
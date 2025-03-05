package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import com.example.kalogatia.data.entities.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseTypeWithExerciseDao {
    @Query("""
    SELECT Exercise.*, ExerciseType.name as exerciseTypeName
    FROM Exercise
    INNER JOIN ExerciseType ON Exercise.exerciseTypeId = ExerciseType.exerciseTypeId
    WHERE Exercise.workoutId = :workoutId
""")
    fun fetchExerciseWithExerciseTypeById(workoutId: Int): Flow<List<ExerciseWithType>>

    @Query("""
    SELECT Exercise.*, ExerciseType.name as exerciseTypeName
    FROM Exercise
    INNER JOIN ExerciseType ON Exercise.exerciseTypeId = ExerciseType.exerciseTypeId
    WHERE Exercise.exerciseId = :exerciseId
""")
    fun fetchExerciseWithExerciseType(exerciseId: Int): Flow<ExerciseWithType>
}

data class ExerciseWithType(
    @Embedded val exercise: Exercise,
    val exerciseTypeName: String  // Holds the name of the ExerciseType
)

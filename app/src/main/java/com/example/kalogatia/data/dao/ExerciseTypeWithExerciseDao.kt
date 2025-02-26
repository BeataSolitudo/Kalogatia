package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import com.example.kalogatia.data.entities.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseTypeWithExerciseDao {
    /*
    @Query("SELECT * FROM ExerciseType INNER JOIN Exercise ON ExerciseType.exerciseTypeId = Exercise.exerciseTypeId")
    fun fetchExerciseWithExerciseTypeById(workoutId: Int): Flow<List<ExerciseTypeWithExercises>>
*/

/*
    @Query("""
    SELECT * FROM ExerciseType 
    INNER JOIN Exercise ON ExerciseType.exerciseTypeId = Exercise.exerciseTypeId 
    WHERE Exercise.workoutId = :workoutId
""")
    fun fetchExerciseWithExerciseTypeById(workoutId: Int): Flow<List<ExerciseTypeWithExercises>>
*/
    /*
    @Query("SELECT * FROM exercise INNER JOIN exercise.ExerciseType ON exerciseType.exerciseTypeId WHERE exercise.workoutId = :workoutId")
    fun fetchExerciseWithExerciseTypeById(workoutId: Int): Flow<List<ExerciseTypeWithExercises>>
*/
    @Query("""
    SELECT Exercise.*, ExerciseType.name as exerciseTypeName
    FROM Exercise
    INNER JOIN ExerciseType ON Exercise.exerciseTypeId = ExerciseType.exerciseTypeId
    WHERE Exercise.workoutId = :workoutId
""")
    fun fetchExerciseWithExerciseTypeById(workoutId: Int): Flow<List<ExerciseWithType>>

}

data class ExerciseWithType(
    @Embedded val exercise: Exercise,
    val exerciseTypeName: String  // Holds the name of the ExerciseType
)

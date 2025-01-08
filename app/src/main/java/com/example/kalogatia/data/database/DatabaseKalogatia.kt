package com.example.kalogatia.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kalogatia.data.dao.ExerciseDao
import com.example.kalogatia.data.dao.ExerciseTypeDao
import com.example.kalogatia.data.dao.HistoryExerciseDao
import com.example.kalogatia.data.dao.HistorySetDao
import com.example.kalogatia.data.dao.HistoryWorkoutDao
import com.example.kalogatia.data.dao.SetDao
import com.example.kalogatia.data.dao.UserDao
import com.example.kalogatia.data.dao.WorkoutDao
import com.example.kalogatia.data.dao.WorkoutPlanningDao
import com.example.kalogatia.data.entities.Exercise
import com.example.kalogatia.data.entities.ExerciseType
import com.example.kalogatia.data.entities.HistoryExercise
import com.example.kalogatia.data.entities.HistorySet
import com.example.kalogatia.data.entities.HistoryWorkout
import com.example.kalogatia.data.entities.Workout
import com.example.kalogatia.data.entities.Set
import com.example.kalogatia.data.entities.User
import com.example.kalogatia.data.entities.WorkoutPlanning

@Database(
    entities = [User::class ,Workout::class, WorkoutPlanning::class, Exercise::class, Set::class, ExerciseType::class, HistoryWorkout::class, HistoryExercise::class, HistorySet::class],
    version = 1
)
abstract class DatabaseKalogatia: RoomDatabase() {
    abstract val userDao: UserDao
    abstract val workoutDao: WorkoutDao
    abstract val workoutPlanningDao: WorkoutPlanningDao
    abstract val exerciseDao: ExerciseDao
    abstract val setDao: SetDao
    abstract val exerciseTypeDao: ExerciseTypeDao
    abstract val historyWorkoutDao: HistoryWorkoutDao
    abstract val historyExerciseDao: HistoryExerciseDao
    abstract val historySetDao: HistorySetDao

    companion object {
        @Volatile // Whenever we change value of valuable INSTANCE, this change is immediately visible to other threads, so it basically prevents race condition
        private var INSTANCE: DatabaseKalogatia? = null

        fun getInstance(context: Context): DatabaseKalogatia {
            synchronized(this) {    // synchronized makes sure that code innit is executed by only one thread
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseKalogatia::class.java,
                    "kalogatia_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
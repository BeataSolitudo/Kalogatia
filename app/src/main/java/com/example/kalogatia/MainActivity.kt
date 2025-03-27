package com.example.kalogatia

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kalogatia.data.database.DatabaseKalogatia
import com.example.kalogatia.data.entities.Exercise
import com.example.kalogatia.data.entities.ExerciseType
import com.example.kalogatia.data.entities.User
import com.example.kalogatia.data.entities.Workout
import com.example.kalogatia.data.entities.Set
import com.example.kalogatia.data.entities.WorkoutPlanning
import com.example.kalogatia.ui.handleNavigation
import com.example.kalogatia.ui.screens.AddExerciseScreen
import com.example.kalogatia.ui.screens.AddWorkoutScreen
import com.example.kalogatia.ui.screens.GraphScreen
import com.example.kalogatia.ui.screens.MainScreen
import com.example.kalogatia.ui.screens.NoteBookScreen
import com.example.kalogatia.ui.screens.RunExerciseScreen
import com.example.kalogatia.ui.screens.SettingsScreen
import com.example.kalogatia.ui.theme.KalogatiaTheme
import com.example.kalogatia.viewmodels.SharedViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

// ctrl + shift + r = replace all

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            KalogatiaTheme {

                    val databaseName = "kalogatia_db"
    //applicationContext.deleteDatabase(databaseName)

    val userDao = DatabaseKalogatia.getInstance(this).userDao
    val workoutDao = DatabaseKalogatia.getInstance(this).workoutDao
    val workoutPlanningDao = DatabaseKalogatia.getInstance(this).workoutPlanningDao
    val exerciseDao = DatabaseKalogatia.getInstance(this).exerciseDao
    val setDao = DatabaseKalogatia.getInstance(this).setDao
    val exerciseTypeDao = DatabaseKalogatia.getInstance(this).exerciseTypeDao
    val historyWorkoutDao = DatabaseKalogatia.getInstance(this).historyWorkoutDao
    val historyExerciseDao = DatabaseKalogatia.getInstance(this).historyExerciseDao
    val historySetDao = DatabaseKalogatia.getInstance(this).historySetDao
    val workoutWithWorkoutPlanningDao = DatabaseKalogatia.getInstance(this).workoutWithWorkoutPlanningDao

    val users = listOf(
        User("superuser", "example@gmail.com", "")
    )

    val workouts = listOf(
        Workout("Back + Biceps + Forearms", userId = 1),
        Workout("Chest + Shoulders + Abs", userId = 1),
        Workout("Legs + Triceps", userId = 1)
    )

    val workoutPlanning = listOf(
        WorkoutPlanning(1, 1, 1),
        WorkoutPlanning(1, 1, 4),
        WorkoutPlanning(2, 1, 2),
        WorkoutPlanning(2, 1, 5),
        WorkoutPlanning(3, 1, 3),
        WorkoutPlanning(3, 1, 6)
    )

    val exerciseTypes = listOf(
        ExerciseType("Pull-up"),
        ExerciseType("Deadlift"),
        ExerciseType("Chest-Supported row"),
        ExerciseType("Lat pull-down"),
        ExerciseType("Omni-grip supported row"),
        ExerciseType("Cable row"),
        ExerciseType("Incline"),
        ExerciseType("Preacher barbell curl"),
        ExerciseType("Barbell curl"),
        ExerciseType("Preacher dumbell bar"),
        ExerciseType("Wrist curl")
    )

//                lifecycleScope.launch {
//                    users.forEach { userDao.upsertUser(it) }
//                    workouts.forEach { workoutDao.insertWorkout(it.name, it.userId)}
//                    //workouts.forEach { workoutDao.upsertWorkout(it) }
//                    workoutPlanning.forEach { workoutPlanningDao.upsertWorkoutPlanning(it) }
//                    exerciseTypes.forEach { exerciseTypeDao.upsertExerciseType(it) }
//
//                    val exercises = listOf(
//                        Exercise(
//                            exerciseTypeId = exerciseTypeDao.selectExerciseTypeByName("Pull-up")?:throw IllegalArgumentException("ExerciseType 'Pull-up' not found"),
//                            restTime = 90,
//                            workoutId = workoutDao.selectWorkoutByName("Back + Biceps + Forearms")?:throw IllegalArgumentException("Didn't find Back + Biceps + Forearms")
//                        ),
//                        Exercise(
//                            exerciseTypeId = exerciseTypeDao.selectExerciseTypeByName("Deadlift")?:0,
//                            90,
//                            workoutId = workoutDao.selectWorkoutByName("Back + Biceps + Forearms")?:0
//                        ),
//                        Exercise(
//                            exerciseTypeId = exerciseTypeDao.selectExerciseTypeByName("Chest-supported row")?:0,
//                            restTime = 90,
//                            workoutId = workoutDao.selectWorkoutByName("Back + Biceps + Forearms")?:0
//                        ),
//                        Exercise(
//                            exerciseTypeId = exerciseTypeDao.selectExerciseTypeByName("Lat pull-down")?:0,
//                            restTime = 90,
//                            workoutId = workoutDao.selectWorkoutByName("Back + Biceps + Forearms")?:0
//                        ),
//                        Exercise(
//                            exerciseTypeId = exerciseTypeDao.selectExerciseTypeByName("Deadlift")?:0,
//                            restTime = 90,
//                            workoutId = workoutDao.selectWorkoutByName("Back + Biceps + Forearms")?:0
//                        ),
//                        Exercise(
//                            exerciseTypeId = exerciseTypeDao.selectExerciseTypeByName("Omni-grip supported row")?:0,
//                            restTime = 90,
//                            workoutId = workoutDao.selectWorkoutByName("Back + Biceps + Forearms")?:0
//                        ),
//                        Exercise(
//                            exerciseTypeId = exerciseTypeDao.selectExerciseTypeByName("Cable row")?:0,
//                            restTime = 90,
//                            workoutId = workoutDao.selectWorkoutByName("Back + Biceps + Forearms")?:0
//                        ),
//                        Exercise(
//                            exerciseTypeId = exerciseTypeDao.selectExerciseTypeByName("Incline")?:0,
//                            restTime = 90,
//                            workoutId = workoutDao.selectWorkoutByName("Back + Biceps + Forearms")?:0
//                        ),
//                        Exercise(
//                            exerciseTypeId = exerciseTypeDao.selectExerciseTypeByName("Preacher barbell curl")?:0,
//                            restTime = 90,
//                            workoutId = workoutDao.selectWorkoutByName("Back + Biceps + Forearms")?:0
//                        ),
//                        Exercise(
//                            exerciseTypeId = exerciseTypeDao.selectExerciseTypeByName("Barbell curl")?:0,
//                            restTime = 90,
//                            workoutId = workoutDao.selectWorkoutByName("Back + Biceps + Forearms")?:0
//                        ),
//                        Exercise(
//                            exerciseTypeId = exerciseTypeDao.selectExerciseTypeByName("Preacher dumbell bar")?:0,
//                            restTime = 90,
//                            workoutId = workoutDao.selectWorkoutByName("Back + Biceps + Forearms")?:0
//                        ),
//                        Exercise(
//                            exerciseTypeId = exerciseTypeDao.selectExerciseTypeByName("Wrist curl")?:0,
//                            restTime = 90,
//                            workoutId = workoutDao.selectWorkoutByName("Back + Biceps + Forearms")?:0
//                        )
//                    )
//
//                    exercises.forEach { exerciseDao.upsertExercise(it) }
//
//                    val sets = listOf(
//                        Set(
//                            setNumber = setDao.autoNumbering(exerciseDao.getExerciseIdByName("Pull-up")?:0),
//                            weight = 85.0,
//                            repetition = 10,
//                            exerciseId = exerciseDao.getExerciseIdByName("Pull-up")?:0
//                        ),
//                        Set(
//                            setNumber = setDao.autoNumbering(exerciseDao.getExerciseIdByName("Pull-up")?:0),
//                            weight = 85.0,
//                            repetition = 9,
//                            exerciseId = exerciseDao.getExerciseIdByName("Pull-up")?:0
//                        ),
//                        Set(
//                            setNumber = setDao.autoNumbering(exerciseDao.getExerciseIdByName("Pull-up")?:0),
//                            weight = 85.0,
//                            repetition = 8,
//                            exerciseId = exerciseDao.getExerciseIdByName("Pull-up")?:0
//                        ),
//                        Set(
//                            setNumber = setDao.autoNumbering(exerciseDao.getExerciseIdByName("Pull-up")?:0),
//                            weight = 85.0,
//                            repetition = 7,
//                            exerciseId = exerciseDao.getExerciseIdByName("Pull-up")?:0
//                        ),
//                        Set(
//                            setNumber = setDao.autoNumbering(exerciseDao.getExerciseIdByName("Pull-up")?:0),
//                            weight = 85.0,
//                            repetition = 6,
//                            exerciseId = exerciseDao.getExerciseIdByName("Pull-up")?:0
//                        ),
//                    )
//                    sets.forEach { setDao.upsertSet(it) }
//                }

                val navController = rememberNavController()
                val sharedViewModel: SharedViewModel = viewModel()
                val theme by sharedViewModel.currentTheme.collectAsState()

                WindowCompat.setDecorFitsSystemWindows(window, false)
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = theme.navigationColor,
                        isNavigationBarContrastEnforced = false)

                    systemUiController.setStatusBarColor(
                        color = Color.Transparent, // Transparent top bar
                        darkIcons = true
                    )
                }

                NavHost(navController = navController, startDestination = "mainScreen/") {
                    composable("mainScreen/") {
                        MainScreen(navController, { route -> handleNavigation(navController, route) }, sharedViewModel)
                    }

                    composable("addWorkoutScreen/{workoutId}") { backStackEntry ->
                        val workoutId = backStackEntry.arguments?.getString("workoutId")?.toIntOrNull()
                        AddWorkoutScreen(navController, { route -> handleNavigation(navController, route) }, workoutId, sharedViewModel)
                    }

                    composable("addExerciseScreen/{exerciseId}/{workoutId}") { backStackEntry ->
                        val workoutId = backStackEntry.arguments?.getString("workoutId")?.toIntOrNull()
                        val exerciseId = backStackEntry.arguments?.getString("exerciseId")?.toIntOrNull()
                        AddExerciseScreen(navController, { route -> handleNavigation(navController, route) }, exerciseId, workoutId, sharedViewModel)
                    }

                    composable("settingsScreen/") {
                        SettingsScreen(navController, { route -> handleNavigation(navController, route) }, sharedViewModel)
                    }

                    composable("notebook/") {
                        NoteBookScreen(navController, { route -> handleNavigation(navController, route) }, sharedViewModel)
                    }

                    composable("runExerciseScreen/{workoutId}") { backStackEntry ->
                        val workoutId = backStackEntry.arguments?.getString("workoutId")?.toInt()
                        workoutId?.let {
                            RunExerciseScreen(navController, { route -> handleNavigation(navController, route) }, sharedViewModel,
                                it
                            )
                        }
                    }

                    composable("graphScreen/") {
                        GraphScreen(navController, { route -> handleNavigation(navController, route) }, sharedViewModel)
                    }
                }

            }
        }
    }
}

fun databseSomething() {

}
package com.example.kalogatia.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kalogatia.data.dao.WorkoutDao
import com.example.kalogatia.data.dao.WorkoutWithWorkoutPlanningDao
import com.example.kalogatia.data.database.DatabaseKalogatia
import com.example.kalogatia.data.entities.Workout
import com.example.kalogatia.data.relations.WorkoutWithWorkoutPlanning
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class MainScreenViewModel(
    private val workoutDao: WorkoutDao,
    private val workoutWithWorkoutPlanningDao: WorkoutWithWorkoutPlanningDao
) : ViewModel() {

    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts

    private val _todayWorkout = MutableStateFlow("Loading...")
    val todayWorkout: StateFlow<String> = _todayWorkout

    private val _workoutsWithPlanning = MutableStateFlow<List<WorkoutWithWorkoutPlanning>>(emptyList())
    val workoutsWithPlanning: StateFlow<List<WorkoutWithWorkoutPlanning>> = _workoutsWithPlanning

    init {
        fetchWorkouts()
        fetchTodayWorkout()
        fetchWorkoutsAndWorkoutPlanning()
    }

    private fun fetchWorkouts() {
        viewModelScope.launch {
            val dbWorkouts = workoutDao.selectAllWorkouts().firstOrNull()
            _workouts.value = dbWorkouts ?: emptyList()
        }
    }

    private fun fetchTodayWorkout() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val dayOfWeek = today.dayOfWeek.value
            _todayWorkout.value = workoutDao.selectWorkoutByDay(dayOfWeek) ?: "Today is rest day!"
        }
    }
/*
    fun getWorkoutDay(workoutId: Int): Int {
        viewModelScope.launch {
            val workoutDay = workoutPlanningDao.getWorkoutDay(workoutId)
        }
    }
*/
    fun fetchWorkoutsAndWorkoutPlanning() {
        viewModelScope.launch {
            val workoutsPlanningData = workoutWithWorkoutPlanningDao.getAllWorkoutsWithWorkoutPlanning().firstOrNull()
            _workoutsWithPlanning.value = workoutsPlanningData ?: emptyList()
        }
    }

    // ViewModel Factory (Companion Object)
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY]) // Getting application instance

                // Get the instance of WorkoutDao
                val workoutDao = DatabaseKalogatia.getInstance(application).workoutDao
                val workoutPlanningDao = DatabaseKalogatia.getInstance(application).workoutPlanningDao
                val workoutWithWorkoutPlanningDao = DatabaseKalogatia.getInstance(application).workoutWithWorkoutPlanningDao

                return MainScreenViewModel(workoutDao, workoutWithWorkoutPlanningDao) as T
            }
        }
    }
}

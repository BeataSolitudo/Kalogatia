package com.example.kalogatia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kalogatia.data.dao.ExerciseDao
import com.example.kalogatia.data.dao.ExerciseTypeWithExerciseDao
import com.example.kalogatia.data.dao.ExerciseWithType
import com.example.kalogatia.data.dao.SetDao
import com.example.kalogatia.data.dao.WorkoutDao
import com.example.kalogatia.data.database.DatabaseKalogatia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddWorkoutScreenViewModel(
    private val exerciseDao: ExerciseDao,
    private val exerciseTypeWithExerciseDao: ExerciseTypeWithExerciseDao,
    private val setDao: SetDao,
    private val workoutId: Int?,
    private val workoutDao: WorkoutDao
) : ViewModel() {
    init {
        workoutId?.let { fetchExercisesForWorkout(it) }
        workoutId?.let { countSetsByExerciseId(it) }
        workoutId?.let { fetchMaxWeight(it) }
        //workoutId?.let { fetchWorkoutName(it) }
    }

    private val _exercisesWithType = MutableStateFlow<List<ExerciseWithType>>(emptyList())
    val exercisesWithType: StateFlow<List<ExerciseWithType>> = _exercisesWithType.asStateFlow()

    fun fetchExercisesForWorkout(workoutId: Int) {
        viewModelScope.launch {
            val exerciseData = exerciseTypeWithExerciseDao.fetchExerciseWithExerciseTypeById(workoutId).firstOrNull()
            _exercisesWithType.value = exerciseData ?: emptyList()
        }
    }

    private val _counters = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val counters: StateFlow<Map<Int, Int>> = _counters.asStateFlow()

    fun countSetsByExerciseId(exerciseId: Int) {
        viewModelScope.launch {
            val count = setDao.countSetsByExerciseId(exerciseId)
            _counters.value = _counters.value.toMutableMap().apply { put(exerciseId, count) }
        }
    }

    private val _maxWeights = MutableStateFlow<Map<Int, Double>>(emptyMap())
    val maxWeights: StateFlow<Map<Int, Double>> = _maxWeights.asStateFlow()

    fun fetchMaxWeight(exerciseId: Int) {
        viewModelScope.launch {
            val maxWeight = setDao.getMaxWeightByExerciseId(exerciseId) ?: 0.0
            _maxWeights.value = _maxWeights.value.toMutableMap().apply { put(exerciseId, maxWeight) }
        }
    }

    private val _workoutName = MutableStateFlow("Loading...") // Default value
    val workoutName: StateFlow<String> = _workoutName.asStateFlow()
/*
    fun fetchWorkoutName(workoutId: Int) {
        viewModelScope.launch {
            val name = workoutDao.getWorkoutName(workoutId) ?: "Workout Name Not Found"
            _workoutName.value = name
        }
    }*/

    fun fetchWorkoutName(workoutId: Int) {
        viewModelScope.launch {
            val name = withContext(Dispatchers.IO) { // ✅ Run database query on a background thread
                workoutDao.getWorkoutName(workoutId) ?: "Workout Name Not Found"
            }
            _workoutName.value = name
        }
    }

    fun insertWorkout(workoutName: String, userId: Int) {
        viewModelScope.launch {
            workoutDao.insertWorkout(workoutName, userId)
        }
    }

    fun updateWorkout(workoutId: Int, workoutName: String, userId: Int) {
        viewModelScope.launch {
            workoutDao.updateWorkout(workoutId, workoutName, userId)
        }
    }

    // ViewModel Factory (Companion Object)
    companion object {
        fun provideFactory(workoutId: Int?): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                    val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                    val dbInstance = DatabaseKalogatia.getInstance(application)
                    return AddWorkoutScreenViewModel(
                        dbInstance.exerciseDao,
                        dbInstance.exerciseTypeWithExerciseDao,
                        dbInstance.setDao,
                        workoutId,
                        dbInstance.workoutDao
                    ) as T
                }
            }
        }
    }
}
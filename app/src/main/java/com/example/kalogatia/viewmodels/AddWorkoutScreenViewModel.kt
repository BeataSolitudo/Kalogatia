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
import com.example.kalogatia.data.dao.WorkoutPlanningDao
import com.example.kalogatia.data.database.DatabaseKalogatia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
    private val workoutDao: WorkoutDao,
    private val workoutPlanningDao: WorkoutPlanningDao
) : ViewModel() {
    init {
        workoutId?.let { fetchExercisesForWorkout(it) }
        workoutId?.let { countSetsByExerciseId(it) }
        workoutId?.let { fetchMaxWeight(it) }
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

    private val _workoutName = MutableStateFlow("Loading...")
    val workoutName: StateFlow<String> = _workoutName.asStateFlow()

    fun fetchWorkoutName(workoutId: Int) {
        viewModelScope.launch {
            val name = withContext(Dispatchers.IO) {
                workoutDao.getWorkoutName(workoutId) ?: "Workout Name Not Found"
            }
            _workoutName.value = name
        }
    }

    private var _newWorkoutId = MutableStateFlow<Int?>(null)
    var newWorkoutId: Flow<Int?> = _newWorkoutId

    fun insertWorkout(workoutName: String, userId: Int) {
        viewModelScope.launch {
            _newWorkoutId.value = workoutDao.insertWorkout(workoutName, userId).toInt()
        }
    }

    fun updateWorkout(workoutId: Int, workoutName: String, userId: Int) {
        viewModelScope.launch {
            workoutDao.updateWorkout(workoutId, workoutName, userId)
        }
    }

    fun cascadeDeleteExercise(exerciseId: Int) {
        viewModelScope.launch {
            setDao.deleteSetByExerciseId(exerciseId)
            exerciseDao.deleteExerciseByExerciseId(exerciseId)
            workoutId?.let { fetchExercisesForWorkout(it) }
        }
    }

    private val _workoutDays = MutableStateFlow<List<Int>?>(emptyList())
    val workoutDays: StateFlow<List<Int>?> = _workoutDays

    fun fetchWorkoutDays(workoutId: Int) {
        viewModelScope.launch {
            workoutPlanningDao.fetchWorkoutDays(workoutId)
                .collect { days ->
                    _workoutDays.value = days
                }
        }
    }

    fun deleteWorkoutDays(workoutId: Int, workoutDays: List<Int>) {
        viewModelScope.launch {
            if (!workoutDays.isEmpty()) {
                workoutPlanningDao.deleteWorkoutPlanning(workoutId, workoutDays)
            }
        }
    }

    fun insertWorkoutDays(workoutId: Int, workoutDays: List<Int>) {
        viewModelScope.launch {
            if (workoutDays.isNotEmpty()) {
                for (weekDay in workoutDays) {
                    workoutPlanningDao.insertWorkoutPlanning(workoutId, 1, weekDay)
                }
            }
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
                        dbInstance.workoutDao,
                        dbInstance.workoutPlanningDao
                    ) as T
                }
            }
        }
    }
}
package com.example.kalogatia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kalogatia.data.dao.ExerciseDao
import com.example.kalogatia.data.dao.ExerciseTypeDao
import com.example.kalogatia.data.dao.SetDao
import com.example.kalogatia.data.database.DatabaseKalogatia
import com.example.kalogatia.data.entities.Exercise
import com.example.kalogatia.data.entities.ExerciseType
import com.example.kalogatia.data.entities.Set
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RunExerciseViewModel(
    private val workoutId: Int,
    private val setDao: SetDao,
    private val exerciseDao: ExerciseDao,
    private val exerciseTypeDao: ExerciseTypeDao
): ViewModel() {
    private var _timer = MutableStateFlow(0)
    val timer: StateFlow<Int> = _timer

    private var timerJob: Job? = null

    fun startTimer(time: Int) {
        if (timerJob?.isActive == true) return

        _timer.value = time
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                _timer.value --
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun resetTimer() {
        stopTimer()
        _timer.value = 0
    }

    // 1️⃣ Store all exercises for the given workout
    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    // 2️⃣ Store sets mapped by exerciseId
    private val _setsMap = MutableStateFlow<Map<Int, List<Set>>>(emptyMap())
    val setsMap: StateFlow<Map<Int, List<Set>>> = _setsMap.asStateFlow()

    // 🔥 Fetch exercises + sets
    fun fetchExercises() {
        viewModelScope.launch {
            val fetchedExercises = exerciseDao.fetchExercisesByWorkoutId(workoutId).first() // 🔥 Collect Flow
            _exercises.value = fetchedExercises // ✅ Now assigns a List<Exercise>
            fetchSetsForExercises(fetchedExercises)
        }
    }

    private fun fetchSetsForExercises(exercises: List<Exercise>) {
        viewModelScope.launch {
            val newSetsMap = mutableMapOf<Int, List<Set>>()
            for (exercise in exercises) {
                val exerciseId = exercise.exerciseId ?: continue // ✅ Skip if null
                val fetchedSets = setDao.fetchSetsByExerciseId(exerciseId).first() // 🔥 Collect Flow
                newSetsMap[exerciseId] = fetchedSets // ✅ Now assigns a List<Set>
            }
            _setsMap.value = newSetsMap // ✅ Correct type
        }
    }

    private val _exerciseTypeName = MutableStateFlow<String?>(null)
    val exerciseTypeName: StateFlow<String?> = _exerciseTypeName.asStateFlow()

    fun fetchExerciseTypeName(exerciseTypeId: Int) {
        viewModelScope.launch {
            val fetchedExerciseType = exerciseTypeDao.fetchExerciseTypeName(exerciseTypeId)
            _exerciseTypeName.value = fetchedExerciseType
        }
    }

    private val _exerciseTypes = MutableStateFlow<List<ExerciseType>>(emptyList())
    val exerciseTypes: StateFlow<List<ExerciseType>> = _exerciseTypes

    fun fetchExerciseTypes() {
        viewModelScope.launch {
            // Fetch all exercise types from the DAO (assuming it returns a Flow)
            val fetchedExerciseTypes = exerciseTypeDao.selectAllExerciseType().first() // Collect the Flow to get the result
            _exerciseTypes.value = fetchedExerciseTypes // Update the state with the fetched list
        }
    }

    private var _click = MutableStateFlow<Boolean>(false)
    val click: StateFlow<Boolean> = _click

    fun setClicked(value: Boolean) {
        _click.value = value
    }

    suspend fun deleteSets(setsIds: List<Int>) {
        setsIds.forEach { setId ->
            setDao.deleteSet(setId)
        }
    }

    suspend fun insertSet(setNumber: Int, weight: Double, repetition: Int, exerciseId: Int) {
        viewModelScope.launch {
            setDao.insertSet(setNumber, weight, repetition, exerciseId)
        }
    }

    suspend fun updateSet(setId: Int, setNumber: Int, weight: Double, repetition: Int) {
        viewModelScope.launch {
            setDao.updateSet(setId, setNumber, weight, repetition)
        }
    }

    companion object {
        fun provideFactory(workoutId: Int): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                    val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                    val dbInstance = DatabaseKalogatia.getInstance(application)
                    return RunExerciseViewModel(
                        workoutId,
                        dbInstance.setDao,
                        dbInstance.exerciseDao,
                        dbInstance.exerciseTypeDao,
                    ) as T
                }
            }
        }
    }
}
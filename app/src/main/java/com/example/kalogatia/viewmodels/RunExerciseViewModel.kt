package com.example.kalogatia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kalogatia.data.dao.ExerciseDao
import com.example.kalogatia.data.dao.ExerciseTypeDao
import com.example.kalogatia.data.dao.HistoryExerciseDao
import com.example.kalogatia.data.dao.HistorySetDao
import com.example.kalogatia.data.dao.HistoryWorkoutDao
import com.example.kalogatia.data.dao.SetDao
import com.example.kalogatia.data.dao.WorkoutDao
import com.example.kalogatia.data.database.DatabaseKalogatia
import com.example.kalogatia.data.entities.Exercise
import com.example.kalogatia.data.entities.ExerciseType
import com.example.kalogatia.data.entities.Set
import com.example.kalogatia.ui.screens.RunSetData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.State

class RunExerciseViewModel(
    private val workoutId: Int,
    private val setDao: SetDao,
    private val exerciseDao: ExerciseDao,
    private val exerciseTypeDao: ExerciseTypeDao,
    private val workoutDao: WorkoutDao,
    private val historyWorkoutDao: HistoryWorkoutDao,
    private val historyExerciseDao: HistoryExerciseDao,
    private val historySetDao: HistorySetDao,
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

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    private val _setsMap = MutableStateFlow<Map<Int, List<Set>>>(emptyMap())
    val setsMap: StateFlow<Map<Int, List<Set>>> = _setsMap.asStateFlow()

    fun fetchExercises() {
        viewModelScope.launch {
            val fetchedExercises = exerciseDao.fetchExercisesByWorkoutId(workoutId).first()
            _exercises.value = fetchedExercises
            fetchSetsForExercises(fetchedExercises)
        }
    }

    private fun fetchSetsForExercises(exercises: List<Exercise>) {
        viewModelScope.launch {
            val newSetsMap = mutableMapOf<Int, List<Set>>()
            for (exercise in exercises) {
                val exerciseId = exercise.exerciseId ?: continue
                val fetchedSets = setDao.fetchSetsByExerciseId(exerciseId).first()
                newSetsMap[exerciseId] = fetchedSets
            }
            _setsMap.value = newSetsMap
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
            val fetchedExerciseTypes = exerciseTypeDao.selectAllExerciseType().first()
            _exerciseTypes.value = fetchedExerciseTypes
        }
    }

    private var _click = MutableStateFlow<Boolean>(false)
    val click: StateFlow<Boolean> = _click

    fun setClicked(value: Boolean) {
        _click.value = value
    }

    private var _click2 = MutableStateFlow<Boolean>(false)
    val click2: StateFlow<Boolean> = _click2

    fun setClicked2(value: Boolean) {
        _click2.value = value
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

    private var _historyWorkoutId = MutableStateFlow<Int?>(null)
    val historyWorkoutId: StateFlow<Int?> = _historyWorkoutId

    suspend fun insertHistoryWorkout(workoutId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val workoutName: String? = workoutDao.getWorkoutName(workoutId)
                val historyWorkoutId = workoutName?.let { historyWorkoutDao.insertHistoryWorkout(it, 1).toInt() }
                _historyWorkoutId.value = historyWorkoutId  // Use `.value` to update StateFlow
                setClicked2(true)
            }
        }
    }

    suspend fun makeCopy(exercise: Exercise, sets: List<RunSetData>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {  // Moves database operations to background thread
                val historyExerciseId= historyWorkoutId.value?.let {
                    historyExerciseDao.insertExercise(exercise.exerciseTypeId, exercise.restTime,
                        it
                    )
                }

                sets.forEach { set ->
                    historyExerciseId.let {
                        it?.let { it1 -> historySetDao.insertSet(set.position, set.weight, set.reps, it1.toInt()) }
                    }
                }
            }
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
                        dbInstance.workoutDao,
                        dbInstance.historyWorkoutDao,
                        dbInstance.historyExerciseDao,
                        dbInstance.historySetDao,
                    ) as T
                }
            }
        }
    }
}
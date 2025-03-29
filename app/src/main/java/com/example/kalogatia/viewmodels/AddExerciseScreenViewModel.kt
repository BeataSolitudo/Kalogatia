package com.example.kalogatia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kalogatia.data.dao.ExerciseDao
import com.example.kalogatia.data.dao.ExerciseTypeDao
import com.example.kalogatia.data.dao.ExerciseTypeWithExerciseDao
import com.example.kalogatia.data.dao.ExerciseWithType
import com.example.kalogatia.data.dao.SetDao
import com.example.kalogatia.data.dao.WorkoutPlanningDao
import com.example.kalogatia.data.database.DatabaseKalogatia
import com.example.kalogatia.data.entities.Exercise
import com.example.kalogatia.data.entities.Set
import com.example.kalogatia.data.entities.WorkoutPlanning
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddExerciseScreenViewModel(
    exerciseId: Int?,
    private val exerciseTypeWithExerciseDao: ExerciseTypeWithExerciseDao,
    private val workoutPlanningDao: WorkoutPlanningDao,
    private val setDao: SetDao,
    private val exerciseTypeDao: ExerciseTypeDao,
    private val exerciseDao: ExerciseDao,
): ViewModel() {
    init {
        if (exerciseId != null) {fetchExerciseAccessories(exerciseId)}
    }

    private val _exercisesWithType = MutableStateFlow<ExerciseWithType?>(null)
    val exercisesWithType: StateFlow<ExerciseWithType?> = _exercisesWithType.asStateFlow()

    private val _weekDay = MutableStateFlow<Int?>(null)
    val weekDay: StateFlow<Int?> = _weekDay.asStateFlow()

    private var _exerciseName = MutableLiveData<String>(null)
    val exerciseName: LiveData<String> = _exerciseName

    private var _sets = MutableStateFlow<List<Set>?>(emptyList())
    val sets: StateFlow<List<Set>?> = _sets

    fun fetchExerciseAccessories(exerciseId: Int) {
        viewModelScope.launch {
            exerciseTypeWithExerciseDao.fetchExerciseWithExerciseType(exerciseId)
                .collect { exerciseData ->
                    _exercisesWithType.value = exerciseData
                }
        }
    }

    fun fetchWorkoutDay(workoutId: Int) {
        viewModelScope.launch {
            workoutPlanningDao.getWorkoutDay(workoutId).collect { day ->
                _weekDay.value = day
            }
        }
    }

    fun fetchSets(exerciseId: Int) {
        viewModelScope.launch {
            setDao.fetchSetsByExerciseId(exerciseId).collect { fetchedSets ->
                _sets.value = fetchedSets
            }
        }
    }

    private var _click = MutableStateFlow<Boolean>(false)
    val click: StateFlow<Boolean> = _click

    fun setClicked(value: Boolean) {
        _click.value = value
    }

    fun insertExerciseType(name: String) {
        viewModelScope.launch {
            exerciseTypeDao.insertExerciseType(name)
        }
    }

    private var _exerciseTypeId = MutableStateFlow<Int?>(null)
    val exerciseTypeId: StateFlow<Int?> = _exerciseTypeId

    fun fetchExerciseTypeId(name: String, onFetched: (Int?) -> Unit) {
        viewModelScope.launch {
            val id = exerciseTypeDao.selectExerciseTypeByName(name)
            _exerciseTypeId.value = id
            onFetched(id)
        }
    }

    private var _newExerciseId = MutableStateFlow<Int?>(null)
    val newExerciseId: Flow<Int?> = _newExerciseId

    fun insertExercise(exerciseTypeId: Int, restTime: Int, workoutId: Int) {
        viewModelScope.launch {
            _newExerciseId.value = exerciseDao.insertExercise(exerciseTypeId, restTime, workoutId).toInt()
        }
    }

    private var _exercise = MutableStateFlow<Exercise?>(null)
    val exercise: StateFlow<Exercise?> = _exercise

    fun fetchExercise(exerciseId: Int) {
        viewModelScope.launch {
            val exercise = exerciseDao.fetchExercise(exerciseId)
            _exercise.value = exercise
        }
    }

    fun updateExerciseType(exerciseTypeName: String, exerciseTypeId: Int) {
        viewModelScope.launch {
            exerciseTypeDao.updateExerciseType(exerciseTypeName, exerciseTypeId)
        }
    }

    fun updateRestTime(exerciseId: Int, restTime: Int) {
        viewModelScope.launch {
            exerciseDao.updateRestTime(exerciseId, restTime)
        }
    }

    private var _workoutPlanning = MutableStateFlow<WorkoutPlanning?>(null)
    val workoutPlanning: StateFlow<WorkoutPlanning?> = _workoutPlanning

    fun fetchWorkoutPlanning(workoutId: Int?) {
        viewModelScope.launch {
            workoutId?.let { workoutPlanningDao.fetchWorkoutPlanning(it) }
        }
    }

    suspend fun updateWeekDay(workoutId: Int, weekDay: Int, newWeekDay: Int) {
        viewModelScope.launch {
            workoutPlanningDao.updateWorkoutPlanningWeekDay(workoutId = workoutId, weekDay = weekDay, newWeekDay = newWeekDay)
        }
    }

    suspend fun insertWorkoutDay(workoutId: Int, userId: Int, weekDay: Int) {
        viewModelScope.launch {
            workoutPlanningDao.insertWorkoutPlanning(workoutId, userId, weekDay)
        }
    }

    suspend fun deleteWorkoutPlanning(workoutId: Int, weekDay: Int) {
        viewModelScope.launch {
            workoutPlanningDao.deleteWorkoutPlanning(workoutId, weekDay)
        }
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
        fun provideFactory(exerciseId: Int?): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                    val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                    val dbInstance = DatabaseKalogatia.getInstance(application)
                    return AddExerciseScreenViewModel(
                        exerciseId,
                        dbInstance.exerciseTypeWithExerciseDao,
                        dbInstance.workoutPlanningDao,
                        dbInstance.setDao,
                        dbInstance.exerciseTypeDao,
                        dbInstance.exerciseDao,
                    ) as T
                }
            }
        }
    }
}

data class changedSet(
    val setNumber: Int?,
    val weight: Double?,
    val repetition: Int?,
    val createdAt: Long?,
    val exerciseId: Int?,
    val setId: Int?
)
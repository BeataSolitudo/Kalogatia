package com.example.kalogatia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kalogatia.data.dao.ExerciseTypeWithExerciseDao
import com.example.kalogatia.data.dao.ExerciseWithType
import com.example.kalogatia.data.dao.SetDao
import com.example.kalogatia.data.dao.WorkoutPlanningDao
import com.example.kalogatia.data.database.DatabaseKalogatia
import com.example.kalogatia.data.entities.Set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Todo: Get exercise name thru id if exists
// Todo: Get rest time if exists
// Todo: Get workout day if exists
// Todo: Get sets if exists

// Fetch only if exerciseId is not null

class AddExerciseScreenViewModel(
    exerciseId: Int?,
    private val exerciseTypeWithExerciseDao: ExerciseTypeWithExerciseDao,
    private val workoutPlanningDao: WorkoutPlanningDao,
    private val setDao: SetDao,
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

    private var _sets = MutableStateFlow<List<Set>>(emptyList())
    val sets: StateFlow<List<Set>> = _sets

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

    fun addSet() {

    }

    fun removeSet() {

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
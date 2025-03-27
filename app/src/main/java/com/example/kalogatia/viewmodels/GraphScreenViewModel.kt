package com.example.kalogatia.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import co.yml.charts.common.model.Point
import com.example.kalogatia.data.dao.ExerciseTypeDao
import com.example.kalogatia.data.dao.HistoryExerciseDao
import com.example.kalogatia.data.dao.HistorySetDao
import com.example.kalogatia.data.dao.SetsLittleHelper
import com.example.kalogatia.data.database.DatabaseKalogatia
import com.example.kalogatia.data.entities.ExerciseType
import com.example.kalogatia.data.entities.HistorySet
import com.example.kalogatia.data.entities.Set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

class GraphScreenViewModel(
    private val exerciseTypeDao: ExerciseTypeDao,
    private val historyExerciseDao: HistoryExerciseDao,
    private val historySetDao: HistorySetDao,

): ViewModel() {

    init {
        fetchExerciseTypes()
    }

    private val _exerciseTypes = MutableStateFlow<List<ExerciseType>>(emptyList())
    val exerciseTypes: StateFlow<List<ExerciseType>> = _exerciseTypes

    fun fetchExerciseTypes() {
        viewModelScope.launch {
            val fetchedExerciseTypes = exerciseTypeDao.selectAllExerciseType().first()
            _exerciseTypes.value = fetchedExerciseTypes
        }
    }

    fun fetchHistoryExercisesByExerciseType(exerciseTypeId: Int) {
        viewModelScope.launch {
            val exercises = historyExerciseDao.fetchHistoryExercisesByExerciseType(exerciseTypeId).first()

            // Extract IDs from the exercises list
            val exerciseIds = exercises.mapNotNull { it?.historyExerciseId }

            if (exerciseIds.isNotEmpty()) {
                // Fetch history sets using the list of exercise IDs
                val sets = historySetDao.fetchHistorySets(exerciseIds)
                sets.collect { historySets ->
                    historySets.forEach { set ->
                        println("History Set: $set")
                    }
                }
            }
        }
    }

//    private var _sets = MutableStateFlow<List<SetsLittleHelper>>(emptyList())
//    val sets: StateFlow<List<SetsLittleHelper>> = _sets
//
//    fun fetchHistorySetsByExerciseType(exerciseTypeId: Int) {
//        viewModelScope.launch {
//            historySetDao.fetchSetsByExerciseType(exerciseTypeId).collect { fetchedSets ->
//                _sets.value = fetchedSets
//            }
//        }
//    }

    private var _sets = MutableStateFlow<List<SetsLittleHelper>>(emptyList())
    val sets: StateFlow<List<SetsLittleHelper>> = _sets

    fun fetchHistorySetsByExerciseType(exerciseTypeId: Int) {
        viewModelScope.launch {
            historySetDao.fetchSetsByExerciseType(exerciseTypeId).collect { fetchedSets ->
                _sets.value = fetchedSets
            }
        }
    }



    companion object {
        fun provideFactory(): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                    val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                    val dbInstance = DatabaseKalogatia.getInstance(application)
                    return GraphScreenViewModel(
                        dbInstance.exerciseTypeDao,
                        dbInstance.historyExerciseDao,
                        dbInstance.historySetDao,
                    ) as T
                }
            }
        }
    }
}
package com.example.kalogatia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kalogatia.data.entities.Set

class SharedViewModel: ViewModel() {

    private var _userId = MutableLiveData<Int>(null)
    val userId: LiveData<Int> = _userId

    private var _workoutId = MutableLiveData<Int>(null)
    val workoutId: LiveData<Int> = _workoutId

    private var _workoutName = MutableLiveData<String>(null)
    val workoutName: LiveData<String> = _workoutName

    private var _tmpWorkoutName = MutableLiveData<String?>(null)
    val tmpWorkoutName: LiveData<String?> = _tmpWorkoutName

    private var _exerciseId = MutableLiveData<List<Int>>(null)
    val exerciseId: LiveData<List<Int>> = _exerciseId

    private var _restTime = MutableLiveData<List<Double>>(null)
    val restTime: LiveData<List<Double>> = _restTime

    private var _exerciseTypeName = MutableLiveData<List<String>>(null)
    val exerciseTypeName: LiveData<List<String>> = _exerciseTypeName

    private var _set = MutableLiveData<List<Set>>(null)
    val set: LiveData<List<Set>> = _set

    fun saveUserId(userId: Int) {
        _userId.value = userId
    }

    fun saveWorkoutId(workoutId: Int) {
        _workoutId.value = workoutId
    }

    fun saveWorkoutName(workoutName: String) {
        _workoutName.value = workoutName
    }

    fun saveTmpWorkoutName(tmpWorkoutName: String?) {
        _tmpWorkoutName.value = tmpWorkoutName
    }

    fun saveExerciseId(exerciseId: Int) {   // Its not list because we are storing only one exercise at a time, we might map it later
        val currentList = _exerciseId.value.orEmpty().toMutableList()
        currentList.add(exerciseId)
        _exerciseId.value = currentList
    }

    fun saveRestTime(restTime: Double) {
        val currentList = _restTime.value.orEmpty().toMutableList()
        currentList.add(restTime)
        _restTime.value = currentList
    }

    fun saveExerciseTypeName(exerciseTypeName: String) {
        val currentList = _exerciseTypeName.value.orEmpty().toMutableList()
        currentList.add(exerciseTypeName)
        _exerciseTypeName.value = currentList
    }

    fun saveSet(set: Set) {
        val currentList = _set.value.orEmpty().toMutableList()
        currentList.add(set)
        _set.value = currentList
    }
}


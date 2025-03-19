package com.example.kalogatia.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kalogatia.DataStoreManager
import com.example.kalogatia.data.entities.Set
import com.example.kalogatia.data.entities.Workout
import com.example.kalogatia.ui.theme.AppColorScheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedViewModel(application: Application): AndroidViewModel(application) {
    private val context: Context = application.applicationContext

    private val _colorfulColorScheme = MutableStateFlow(
        AppColorScheme(
            backgroundColor = Color(0xFF0E0E0E),
            textColor = Color.White,
            navigationColor = Color(0xFF25283C),
            unselectedNavigationItemColor =  Color.Black.copy(alpha = 0.5f),
            selectedNavigationItemColor = Color(0xFFFF4081),
            cellColor = Color(0xFF282828),
            cellColorGradient = Color(0xFFCA56CB),
            cellColorGradient2 = Color(0xFF7359F2),
            iconColor = Color.White,
            dividerColor = Color.White,
            textFieldBackgroundColor = Color(0xFF282639),
            borderColorGradient = Color(0xFF6A00F4),
            borderColorGradient2 = Color(0xFFF40072),
            customButtonColor = Color(0xFF363352)
        )
    )

    private val _whiteColorScheme = MutableStateFlow(
        AppColorScheme(
            backgroundColor = Color(0xFFD6E1E6),
            textColor = Color.Black,
            navigationColor = Color(0xFFF0F0F0),
            unselectedNavigationItemColor =  Color.Black.copy(alpha = 0.5f),
            selectedNavigationItemColor = Color(0xFFFF4081),
            cellColor = Color(0xFFFAFAFA),
            cellColorGradient = Color(0xFFFAFAFA),
            cellColorGradient2 = Color(0xFFFAFAFA),
            iconColor = Color.Black,
            dividerColor = Color.Black,
            textFieldBackgroundColor = Color(0xFFFAFAFA),
            borderColorGradient = Color(0xFF0096ff),
            borderColorGradient2 = Color(0xFF87d4f5),
            customButtonColor = Color(0xFF87d4f5)
        )
    )

    private val _darkColorScheme = MutableStateFlow(
        AppColorScheme(
            backgroundColor = Color(0xFF121212),
            textColor = Color.White,
            navigationColor = Color(0xFF282828),
            unselectedNavigationItemColor =  Color.Black.copy(alpha = 0.5f),
            selectedNavigationItemColor = Color(0xFF85b7ff),
            cellColor = Color(0xFF282828),
            cellColorGradient = Color(0xFF282828),
            cellColorGradient2 = Color(0xFF282828),
            iconColor = Color(0xFF85b7ff),
            dividerColor = Color.White,
            textFieldBackgroundColor = Color(0xFF282828),
            borderColorGradient = Color(0xFF0096ff),
            borderColorGradient2 = Color(0xFF001726),
            customButtonColor = Color(0xFF363352)
        )
    )

    private val _currentTheme = MutableStateFlow<AppColorScheme>(_colorfulColorScheme.value)
    val currentTheme: StateFlow<AppColorScheme> = _currentTheme

    init {
        // Continuously observe the theme from DataStore
        viewModelScope.launch {
            DataStoreManager.getTheme(context).collect { theme ->
                when (theme) {
                    "dark" -> _currentTheme.value = _darkColorScheme.value
                    "white" -> _currentTheme.value = _whiteColorScheme.value
                    "colorful" -> _currentTheme.value = _colorfulColorScheme.value
                    else -> _currentTheme.value = _colorfulColorScheme.value
                }
            }
        }
    }

    private val _fetchedWorkout = MutableStateFlow<Workout?>(null)
    val fetchedWorkout: StateFlow<Workout?> = _fetchedWorkout

    private var _selectedWorkoutId = MutableStateFlow<Int?>(null)
    val selectedWorkoutId: StateFlow<Int?> = _selectedWorkoutId

    private val _bottomSheetVisible = MutableStateFlow<Boolean>(false)
    val bottomSheetVisible: StateFlow<Boolean> = _bottomSheetVisible

    fun showBottomSheet(workoutId: Int) {
        _bottomSheetVisible.value = true
        _selectedWorkoutId.value = workoutId
        println("SharedViewModel shownBottomSheet")
    }

    fun hideBottomSheet() {
        _bottomSheetVisible.value = false
        _selectedWorkoutId.value = null
    }


































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

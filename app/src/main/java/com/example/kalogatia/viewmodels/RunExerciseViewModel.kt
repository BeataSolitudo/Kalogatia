package com.example.kalogatia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RunExerciseViewModel: ViewModel() {
    private var _timer = MutableStateFlow(0)
    val timer: StateFlow<Int> = _timer

    private var timerJob: Job? = null

    fun startTimer(time: Int) {
        if (timerJob?.isActive == true) return // ✅ Prevent multiple coroutines

        _timer.value = time
        timerJob = viewModelScope.launch {
            while (true) {  // ✅ Allow timer to go negative
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
}
package com.example.kalogatia.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AddWorkoutScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    )  {
        Box(
            modifier = Modifier
                .weight(0.18f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) { Text(text = "Add Workout") }
        Box(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {  }
        Box(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {  }
    }
}

@Composable
fun AddWorkout() {

}

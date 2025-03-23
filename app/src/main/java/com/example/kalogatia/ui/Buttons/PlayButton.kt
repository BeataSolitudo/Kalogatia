package com.example.kalogatia.ui.Buttons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kalogatia.viewmodels.SharedViewModel

/* possible colour 7CFC00 */
/* old 4CAF50 */
@Composable
fun PlayButton(navController: NavController, workoutId: Int) {
    val sharedViewModel: SharedViewModel = viewModel()

    Box(
        modifier = Modifier
            .size(55.dp)
            .background(
                color = Color(0xFF0FFF50),
                shape = CircleShape
            ).clickable { navController.navigate("runExerciseScreen/$workoutId") },

        contentAlignment = Alignment.Center
    ) {
        DrawPlayIcon()
    }
}

@Composable
fun DrawPlayIcon() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(size.width * 0.35f, size.height * 0.25f) // Top point
            lineTo(size.width * 0.35f, size.height * 0.75f) // Bottom point
            lineTo(size.width * 0.75f, size.height * 0.5f) // Right point
            close() // Close the path to form a triangle
        }
        drawPath(
            path = path,
            color = Color.White
        )
    }
}
package com.example.kalogatia.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kalogatia.data.entities.Exercise
import com.example.kalogatia.data.entities.Workout
import com.example.kalogatia.ui.screens.ChangeName
import com.example.kalogatia.ui.screens.Exercise
import com.example.kalogatia.ui.screens.TopBarAddWorkout
import com.example.kalogatia.ui.screens.Workout

/*
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBarLayout(modifier: Modifier = Modifier, workoutDao: WorkoutDao?, currentScreen: String) {
    when (currentScreen) {
        "mainScreen" -> {
            if (workoutDao != null) {
                TopBarMainScreen(modifier, workoutDao)
            } else {

            }
        }
        "addWorkoutScreen" -> TopBarAddWorkoutScreen(modifier)
        "addExerciseScreen" -> TopBarAddExerciseScreen(modifier)
        else -> {
            Text(text = "Nothing Found...")
        }
    }
}
*/

@Composable
inline fun <reified T> ContentLayout(modifier: Modifier = Modifier, items: List<T>, currentScreen: String) {

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (items.isEmpty()) {
                when(currentScreen) {
                    "mainScreen" -> NotFound("You have no workouts!")
                    "addWorkoutScreen" -> NotFound("You have no exercises!")
                    else -> NotFound("You have no records!")
                }
            }
            items.forEach { item ->
                when (item) {
                    is Workout -> {
                        Workout(workoutName = item.name, workoutDay = "Day 1")
                    }
                    is Exercise -> {
                        Exercise(exerciseName = "TODO")
                    }
                    else -> {
                        Text(text = "Unknown item type")
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationLayout(modifier: Modifier = Modifier, navController: NavController, onNavigate: (String) -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(Color(0xFF25283C)),
        contentAlignment = Alignment.Center
    ) {
        Navigation(navController, onNavigate)
    }
}

@Composable
fun Divider() {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val topPadding = screenHeight / 22

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(2.dp)
                .background(Color.White)
        )
    }

    Spacer(modifier = Modifier.height(topPadding.dp))
}


@Composable
fun TopBarAddWorkoutScreen(modifier: Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    var workoutName by remember { mutableStateOf("Type workout name") }
    // Top Bar - Text, PLayButton
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        TopBarAddWorkout(
            workoutName = workoutName,
            onTextClick = { showDialog = true }
        )
    }

    if (showDialog) {
        ChangeName(
            onDismiss = { showDialog = false },
            onEnter = { newName ->
                workoutName = newName
                showDialog = false
            }
        )
    }
}


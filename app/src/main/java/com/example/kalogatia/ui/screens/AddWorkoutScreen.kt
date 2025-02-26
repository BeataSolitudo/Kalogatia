package com.example.kalogatia.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kalogatia.data.dao.ExerciseWithType
import com.example.kalogatia.ui.Buttons.PlayButton
import com.example.kalogatia.ui.Divider
import com.example.kalogatia.ui.NavigationLayout
import com.example.kalogatia.ui.NotFound
import com.example.kalogatia.viewmodels.AddWorkoutScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddWorkoutScreen(
    navController: NavController,
    onNavigate: (String) -> Unit,
    workoutId: Int?,
    viewModel: AddWorkoutScreenViewModel = viewModel(factory = AddWorkoutScreenViewModel.provideFactory(workoutId)),
) {
    val exercisesWithType by viewModel.exercisesWithType.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E))
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        TopBarAddWorkoutScreen(modifier = Modifier.weight(0.15f), viewModel, workoutId)
        Divider()
        AddWorkoutScreenContent(modifier = Modifier.weight(0.75f), exercisesWithType, viewModel)
        NavigationLayout(modifier = Modifier.weight(0.10f), navController, onNavigate)
    }
}

@Composable
fun TopBarAddWorkoutScreen(modifier: Modifier, viewModel: AddWorkoutScreenViewModel, workoutId: Int?) {
    var showDialog by remember { mutableStateOf(false) }
    var workoutName by remember { mutableStateOf("Type workout name") }

    LaunchedEffect(workoutId) {
        workoutId?.let { viewModel.fetchWorkoutName(it) }
    }
    val fetchedWorkoutName by viewModel.workoutName.collectAsState()

    if (workoutId == null) {
        workoutName = "Type workout name"
    } else {
        //workoutName = "Workout Name"
        workoutName = fetchedWorkoutName
    }
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

@Composable
fun TopBarAddWorkout(workoutName: String, onTextClick: () -> Unit) {
    val isDefaultName = workoutName == "Type workout name"

    val infiniteTransition = rememberInfiniteTransition()
    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color.White,
        targetValue = Color(0xFF9C27B0),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(text = "Exercises", fontSize = 40.sp, fontWeight = FontWeight(weight = 800), color = Color.White)
                Text(
                    text = workoutName,
                    color = if (isDefaultName) animatedColor else Color.White,
                    modifier = Modifier.clickable { onTextClick() }
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxSize()
                .padding(end = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            PlayButton()
        }
    }
}

@Composable
fun Exercise(
    exerciseWithType: ExerciseWithType,
    viewModel: AddWorkoutScreenViewModel
    ) {
    LaunchedEffect(exerciseWithType.exercise.exerciseId) {
        exerciseWithType.exercise.exerciseId?.let {
            viewModel.countSetsByExerciseId(it)
            viewModel.fetchMaxWeight(it)
        }
    }


    //val counter by viewModel.counter.collectAsState()
    val counters by viewModel.counters.collectAsState()
    val counter = counters[exerciseWithType.exercise.exerciseId] ?: 0

    val maxWeights by viewModel.maxWeights.collectAsState()
    val maxWeight = maxWeights[exerciseWithType.exercise.exerciseId] ?: 0.0


    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(70.dp)
            .background(
                color = Color(0xFFCA56CB),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF7359F2),
                        Color.Transparent
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 0f)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 2.dp),
                contentAlignment = Alignment.TopStart,
            ) {
                // Exercise name
                Text(
                    text = exerciseWithType.exerciseTypeName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.25f),
                contentAlignment = Alignment.Center,
            ) {
                // Max weight
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$maxWeight kg",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Max", color = Color.White)
                }

            }
            Box(
                modifier = Modifier
                    .weight(0.25f),
                contentAlignment = Alignment.Center
            ) {
                // Number of sets
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$counter",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Sets", color = Color.White)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeName(onDismiss: () -> Unit, onEnter: (String) -> Unit) {
    var textFieldValue by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface (
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Enter Workout Name",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 15.dp, bottom = 8.dp)
                )

                TextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    placeholder = { Text("Workout Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        onEnter(textFieldValue)
                        onDismiss()
                    }) {
                        Text("Enter")
                    }
                }
            }
        }
    }
}

@Composable
fun AddWorkoutScreenContent(
    modifier: Modifier,
    exercises: List<ExerciseWithType>,
    viewModel: AddWorkoutScreenViewModel
) {
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
            if (exercises.isEmpty()) {
                    NotFound("You have no exercises!")
            } else {
                exercises.forEach { exercise ->
                    Exercise(exercise, viewModel)
                }
            }
        }
    }
}

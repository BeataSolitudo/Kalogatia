package com.example.kalogatia.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.kalogatia.ui.theme.AppColorScheme
import com.example.kalogatia.viewmodels.AddWorkoutScreenViewModel
import com.example.kalogatia.viewmodels.SharedViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddWorkoutScreen(
    navController: NavController,
    onNavigate: (String) -> Unit,
    workoutId: Int?,
    sharedViewModel: SharedViewModel
) {
    val viewModel: AddWorkoutScreenViewModel = viewModel(factory = AddWorkoutScreenViewModel.provideFactory(workoutId))
    val exercisesWithType by viewModel.exercisesWithType.collectAsState()
    val theme by sharedViewModel.currentTheme.collectAsState()

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            if ( backStackEntry.destination.route != "addExerciseScreen/{exerciseId}" && backStackEntry.destination.route != "addWorkoutScreen/{workoutId}") {
                sharedViewModel.saveTmpWorkoutName(null)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        TopBarAddWorkoutScreen(modifier = Modifier.weight(0.15f), viewModel, workoutId, sharedViewModel, theme, navController)
        Divider(modifier = Modifier.background(theme.dividerColor))
        AddWorkoutScreenContent(modifier = Modifier.weight(0.75f), exercisesWithType, viewModel, navController, theme, workoutId)
        NavigationLayout(modifier = Modifier.weight(0.10f), navController, onNavigate, theme, workoutId)
    }
}

@Composable
fun Exercise(
    exerciseWithType: ExerciseWithType,
    viewModel: AddWorkoutScreenViewModel,
    onExerciseClick: (Int) -> Unit,
    theme: AppColorScheme
    ) {
    LaunchedEffect(exerciseWithType.exercise.exerciseId) {
        exerciseWithType.exercise.exerciseId?.let {
            viewModel.countSetsByExerciseId(it)
            viewModel.fetchMaxWeight(it)
        }
    }

    val counters by viewModel.counters.collectAsState()
    val counter = counters[exerciseWithType.exercise.exerciseId] ?: 0

    val maxWeights by viewModel.maxWeights.collectAsState()
    val maxWeight = maxWeights[exerciseWithType.exercise.exerciseId] ?: 0.0


    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(70.dp)
            .background(
                color = theme.cellColorGradient,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        theme.cellColorGradient2,
                        Color.Transparent
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 0f)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
            .clickable { exerciseWithType.exercise.exerciseId?.let { onExerciseClick(it) } }
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
                    color = theme.textColor,
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
                        color = theme.textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Max", color = theme.textColor)
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
                        color = theme.textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Sets", color = theme.textColor)
                }

            }
        }
    }
}

@Composable
fun TopBarAddWorkoutScreen(modifier: Modifier, viewModel: AddWorkoutScreenViewModel, workoutId: Int?, sharedViewModel: SharedViewModel, theme: AppColorScheme, navController: NavController) {
    val openDialog = remember { mutableStateOf(false) }
    var workoutName by remember { mutableStateOf("Type workout name") }
    val fetchedWorkoutName by viewModel.workoutName.collectAsState()

    sharedViewModel.saveWorkoutName(workoutName)

    // Fetch workout name only if workoutId is not null
    LaunchedEffect(workoutId) {
        if (workoutId != null) {
            viewModel.fetchWorkoutName(workoutId)
        } else {
            workoutName = "Type workout name"
            openDialog.value = true
        }
    }

    // Update workout name when fetchedWorkoutName changes
    LaunchedEffect(fetchedWorkoutName) {
        if (workoutId != null && fetchedWorkoutName.isNotEmpty()) {
            workoutName = fetchedWorkoutName
        }
    }


    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
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
                    Text(text = "Exercises", fontSize = 40.sp, fontWeight = FontWeight(weight = 800), color = theme.textColor)
                    Text(
                        text = sharedViewModel.tmpWorkoutName.value?:workoutName,
                        color = theme.textColor,
                        modifier = Modifier.clickable {
                            openDialog.value = true
                        }
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
                workoutId?.let { PlayButton(navController, it) }
            }
        }
    }

    if (openDialog.value) {
        DialogWithInput(
            onDismissRequest = {
                openDialog.value = false
                if(workoutId == null) navController.navigate("mainScreen/") },
            onConfirmation = { newWorkoutName ->
                sharedViewModel.saveTmpWorkoutName(newWorkoutName)
                openDialog.value = false
                if (workoutId != null) {
                    viewModel.updateWorkout(workoutId, newWorkoutName, 1)
                } else {
                    viewModel.insertWorkout(newWorkoutName, 1)
                    println("Insert Successful")
                }
            }
        )
    }
}

@Composable
fun DialogWithInput(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf("") }
    val isInputValid = textFieldValue.isNotBlank()

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
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
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = { onConfirmation(textFieldValue) },
                        enabled = isInputValid // Disable button if input is invalid
                    ) {
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
    viewModel: AddWorkoutScreenViewModel,
    navController: NavController,
    theme: AppColorScheme,
    workoutId: Int?
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
                    Exercise(
                        exercise,
                        viewModel,
                        onExerciseClick = { exerciseId ->
                            if (workoutId != null) {
                                navController.navigate("addExerciseScreen/$exerciseId/$workoutId")
                            } else {
                                println("workoutId is null! Cannot navigate.")
                            }
                        },
                        theme
                    )

                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { navController.navigate("addExerciseScreen/{exerciseId}/$workoutId") },
                containerColor = theme.borderColorGradient2,
                modifier = Modifier.padding(bottom = 15.dp, end = 15.dp)
            ) {
                Icon(Icons.Filled.Add, "Add Exercise", tint = theme.textColor)
            }
        }
    }
}

package com.example.kalogatia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kalogatia.data.entities.Workout
import com.example.kalogatia.data.relations.WorkoutWithWorkoutPlanning
import com.example.kalogatia.ui.components.AutoResizedText
import com.example.kalogatia.ui.components.Divider
import com.example.kalogatia.ui.components.NavigationLayout
import com.example.kalogatia.ui.components.NotFound
import com.example.kalogatia.ui.theme.AppColorScheme
import com.example.kalogatia.viewmodels.MainScreenViewModel
import com.example.kalogatia.viewmodels.SharedViewModel

@Composable
fun MainScreen(
    navController: NavController,
    onNavigate: (String) -> Unit,
    sharedViewModel: SharedViewModel,
    viewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.Factory),
) {
    val todayWorkout by viewModel.todayWorkout.collectAsState()
    val workoutsWithPlanning by viewModel.workoutsWithPlanning.collectAsState()
    val incompleteWorkouts by viewModel.incompleteWorkouts.collectAsState()
    val theme by sharedViewModel.currentTheme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        TopBarMainScreen(modifier = Modifier.weight(0.15f), todayWorkout, theme)
        Divider(modifier = Modifier.background(theme.dividerColor))
        MainScreenContent(modifier = Modifier.weight(0.75f), workoutsWithPlanning, incompleteWorkouts, viewModel, navController, theme)
        NavigationLayout(modifier = Modifier.weight(0.10f), navController, onNavigate,theme)
    }
}

@Composable
fun TodayWorkout(workoutName: String,theme: AppColorScheme) {
    Column {
        AutoResizedText(workoutName, style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight(800), color = theme.textColor))
        Text(text = "Today's workout", color = theme.textColor)
    }
}

@Composable
fun Workout(
    workout: Workout,
    workoutDay: String,
    onWorkoutClick: (Int) -> Unit,
    theme: AppColorScheme,
    viewModel: MainScreenViewModel
) {
    var expanded by remember { mutableStateOf(false) }

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
            .padding(16.dp)
            .clickable { workout.workoutId?.let { onWorkoutClick(it) } },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.7f),
                contentAlignment = Alignment.TopStart
            ) {
                AutoResizedText(workout.name, style = TextStyle(color = theme.textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold))
            }
            Box(
                modifier = Modifier
                    .weight(0.2f),
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    text = workoutDay,
                    color = theme.textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.1f)
                    .clickable { expanded = true },
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(Icons.Filled.MoreVert, "Remove workout", tint = theme.textColor)
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(70.dp)
                        .height(40.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Delete") },
                        onClick = {
                            expanded = false
                            workout.workoutId?.let { viewModel.cascadeDeleteWorkout(it) }
                        },
                        modifier = Modifier.height(25.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TopBarMainScreen(modifier: Modifier, workoutName: String, theme: AppColorScheme) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val topPadding = screenHeight / 24

    // Top bar layout
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Workout display section
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxSize()
                    .padding(start = 40.dp, top = topPadding.dp)
            ) {
                TodayWorkout(workoutName = workoutName, theme)
            }

            // Button section
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxSize()
                    .padding(end = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                //PlayButton()
            }
        }
    }
}

@Composable
fun MainScreenContent(
    modifier: Modifier,
    workouts: List<WorkoutWithWorkoutPlanning>,
    incompleteWorkouts: List<Workout>,
    viewModel: MainScreenViewModel,
    navController: NavController,
    theme: AppColorScheme
) {
    // Mapping week numbers (1-7) to their respective names
    val weekDays = mapOf(
        1 to "Mon", 2 to "Tue", 3 to "Wed", 4 to "Thu",
        5 to "Fri", 6 to "Sat", 7 to "Sun"
    )

    // Sorting workouts by the weekday
    val sortedWorkouts = workouts.sortedBy { it.workoutPlanning.weekDay }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Display sorted workouts
            if (sortedWorkouts.isEmpty() && incompleteWorkouts.isEmpty()) {
                NotFound("You have no workouts!")
            } else {
                sortedWorkouts.forEach { workoutWithPlanning ->
                    val dayName = weekDays[workoutWithPlanning.workoutPlanning.weekDay] ?: "Unknown"
                    Workout(
                        workout = workoutWithPlanning.workout,
                        workoutDay = dayName,
                        onWorkoutClick = { navController.navigate("addWorkoutScreen/${workoutWithPlanning.workout.workoutId}") },
                        theme,
                        viewModel
                    )
                }
            }

            // Display incomplete workouts after sorted ones
            if (incompleteWorkouts.isNotEmpty()) {
                incompleteWorkouts.forEach { workout ->
                    Workout(
                        workout = workout,
                        workoutDay = "",
                        onWorkoutClick = { navController.navigate("addWorkoutScreen/${workout.workoutId}") },
                        theme,
                        viewModel
                    )
                }
            }
        }
    }
}

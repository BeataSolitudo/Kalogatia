package com.example.kalogatia.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kalogatia.data.Exercise
import com.example.kalogatia.data.Workout

// Define global workout variable
var workout1: Workout = Workout(
    workoutId = 1,
    workoutName = "Back + Biceps",
    exercises = listOf(
        Exercise(
            exerciseId = 1,
            exerciseName = "Stretch",
            restTime = 240,
            sets = listOf(1),
            weight = listOf(1.0)
        ),
        Exercise(
            exerciseId = 2,
            exerciseName = "Wide pull-up",
            restTime = 90,
            sets = listOf(10, 10, 10, 10, 10),
            weight = listOf(0.0, 0.0, 0.0)
        ),
        Exercise(
            exerciseId = 3,
            exerciseName = "Deadlift",
            restTime = 90,
            sets = listOf(10, 10, 10, 9, 5),
            weight = listOf(70.0, 100.0, 110.0, 120.0, 130.0)
        )
    ),
    workoutDay = "Mon"
)

var workout2: Workout = Workout(
    workoutId = 2,
    workoutName = "Chest + Workout",
    exercises = listOf(
        Exercise(
            exerciseId = 4,
            exerciseName = "Bench Press",
            restTime = 90,
            sets = listOf(12, 10, 8),
            weight = listOf(60.0, 65.0, 70.0)
        ),
        Exercise(
            exerciseId = 5,
            exerciseName = "Dumbbell Flyes",
            restTime = 90,
            sets = listOf(12, 10, 10),
            weight = listOf(15.0, 17.5, 17.5)
        )
    ),
    workoutDay = "Tue"
)

var workout3: Workout = Workout(
    workoutId = 6,
    workoutName = "Legs + Tricpes",
    exercises = listOf(
        Exercise(
            exerciseId = 3,
            exerciseName = "Squats",
            restTime = 90,
            sets = listOf(10, 10, 10, 10, 8),
            weight = listOf(70.0, 100.0, 110.0, 120.0, 130.0)
        ),
        Exercise(
            exerciseId = 7,
            exerciseName = "Leg extensions",
            restTime = 90,
            sets = listOf(10, 10, 10, 10, 10),
            weight = listOf(55.0, 60.0, 60.0, 60.0, 60.0)
        )
    ),
    workoutDay = "Wed"
)

@Composable
fun MainScreen(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        // Top Box - TodayWorkout, AddButton, StartButton
        Box(
            modifier = Modifier
                .weight(0.18f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopStart
        ) {
            TopPart(onClick)
        }

        // MiddleBox - Workouts
        Box(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // AddWorkout()
                Workout(workout1.workoutName, workout1.workoutDay)
                Workout(workout2.workoutName, workout2.workoutDay)
                Workout(workout3.workoutName, workout3.workoutDay)
                Workout(workout1.workoutName, workout1.workoutDay)
                Workout(workout2.workoutName, workout2.workoutDay)
                Workout(workout3.workoutName, workout3.workoutDay)
            }
        }

        // BottomBox - Graph
        Box(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "TODO: Graph")
        }
    }
}

@Composable
fun TopPart(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxSize().padding(start = 30.dp, top = 30.dp)
        ) {
            TodayWorkout(workout1.workoutName)
        }
        Box(
            modifier = Modifier.weight(0.2f).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AddWorkoutButton(onClick)
        }
        Box(
            modifier = Modifier.weight(0.2f).fillMaxSize().padding(end = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            PlayButton()
        }
    }
}

@Composable
fun TodayWorkout(workoutName: String = "N/A") {
    Column {
        Text(text = workoutName, fontSize = 30.sp, fontWeight = FontWeight(weight = 800))
        Text(text = "Today's workout")
    }
}

@Composable
fun AddWorkoutButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(55.dp)
            .shadow(17.dp, shape = CircleShape, ambientColor = Color(0xFF000000), spotColor = Color(0xFF000000))
            .background(color = Color(0xFF57bff3), shape = CircleShape)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.background(Color.Black).height(30.dp).width(4.dp)
        )
        Box(
            modifier = Modifier.background(Color.Black).height(4.dp).width(30.dp)
        )
    }
}

@Composable
fun PlayButton() {
    Box(
        modifier = Modifier
            .size(55.dp)
            .background(
                color = Color(0xFF4CAF50),
                shape = CircleShape
            ),
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

@Composable
fun Workout(workoutName: String = "N/A", workoutDay: String = "N/A") {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(70.dp)
            .background(
                color = Color(0xFFd9dfe0),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFa2ddfa),
                        Color.Transparent
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 0f)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.7f),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = workoutName,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.3f),
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    text = workoutDay,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

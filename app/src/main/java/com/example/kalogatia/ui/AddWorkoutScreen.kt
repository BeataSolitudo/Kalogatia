package com.example.kalogatia.ui

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.kalogatia.ui.Buttons.PlayButton

@Composable
fun AddWorkoutScreen(navController: NavController, onNavigate: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var workoutName by remember { mutableStateOf("Type workout name") }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E)),
    ) {
        // Top Bar - Text, PLayButton
        Box(
            modifier = Modifier
                .weight(0.18f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopStart
        ) {
            TopBarAddWorkout(
                workoutName = workoutName,
                onTextClick = { showDialog = true }
            )
        }

        // Content - Workouts
        Box(
            modifier = Modifier
                .weight(0.72f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Content()
            }
        }

        // Bottom Bar - Navigation
        Box(
            modifier = Modifier
                .weight(0.10f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(Color(0xFF25283C)),
            contentAlignment = Alignment.Center
        ) {
            Navigation(navController, onNavigate)
        }

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
fun Content() {
    Exercise()
}

@Composable
fun Exercise(
    id: Int = 0,
    icon: String = "N/A",
    exerciseName: String = "N/A",
    restTime: Int = 0,
    sets: List<Int> = listOf(0),
    weight: List<Int> = listOf(0)
    ) {
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
                    text = exerciseName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.3f),
                contentAlignment = Alignment.TopEnd
            ) {

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
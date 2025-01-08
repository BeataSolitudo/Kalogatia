package com.example.kalogatia.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kalogatia.data.dao.WorkoutDao
import com.example.kalogatia.data.entities.Workout
import com.example.kalogatia.ui.Buttons.PlayButton
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


// for icons #AAFF00


@Composable
fun MainScreen(
    navController: NavController,
    onNavigate: (String) -> Unit,
    workoutDao: WorkoutDao
) {
    val coroutineScope = rememberCoroutineScope()
    val workouts = remember { mutableStateListOf<Workout>() }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val dbWorkouts = workoutDao.selectAllWorkouts().firstOrNull()
            if (dbWorkouts != null) {
                workouts.clear()
                workouts.addAll(dbWorkouts)
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E)),
    ) {
        // Top Bar - TodayWorkout, AddButton, StartButton
        TopBar(modifier = Modifier.weight(0.18f))

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
                workouts.forEach { workout ->
                    Workout(
                        workoutName = workout.name,
                        workoutDay = "Day ${(workout.workoutId ?: 0)}" // Example Day Placeholder
                    )
                }
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
}

@Composable
fun TopBar(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxSize().padding(start = 40.dp, top = 30.dp)
            ) {
                TodayWorkout(workoutName = "Chest + Abs")
            }
            Box(
                modifier = Modifier.weight(0.3f).fillMaxSize().padding(end = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                PlayButton()
            }
        }
    }
}

@Composable
fun TodayWorkout(workoutName: String = "N/A") {
    Column {
        Text(text = workoutName, fontSize = 30.sp, fontWeight = FontWeight(weight = 800), color = Color.White)
        Text(text = "Today's workout", color = Color.White)
    }
}

@Composable
fun Workout(workoutName: String = "N/A", workoutDay: String = "N/A") {
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
                    text = workoutName,
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
                Text(
                    text = workoutDay,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}



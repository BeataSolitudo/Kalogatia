package com.example.kalogatia.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import com.example.kalogatia.ui.AutoResizedText
import com.example.kalogatia.ui.Buttons.PlayButton
import com.example.kalogatia.ui.ContentLayout
import com.example.kalogatia.ui.Divider
import com.example.kalogatia.ui.NavigationLayout
import com.example.kalogatia.viewmodels.MainScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    navController: NavController,
    onNavigate: (String) -> Unit,
    viewModel: MainScreenViewModel
) {
    val workouts by viewModel.workouts.collectAsState()
    val todayWorkout by viewModel.todayWorkout.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E))
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        TopBarMainScreen(modifier = Modifier.weight(0.15f), todayWorkout)
        Divider()
        ContentLayout(modifier = Modifier.weight(0.75f), workouts, navController.currentBackStackEntry?.destination?.route ?: "Unknown")
        NavigationLayout(modifier = Modifier.weight(0.10f), navController, onNavigate)
    }
}

@Composable
fun TodayWorkout(workoutName: String) {
    Column {
        AutoResizedText(workoutName, style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight(800), color = Color.White))
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
                AutoResizedText(workoutName, style = TextStyle(color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold))
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBarMainScreen(modifier: Modifier, workoutName: String) {
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
                TodayWorkout(workoutName = workoutName)
            }

            // Button section
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
}

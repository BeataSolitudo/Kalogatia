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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kalogatia.ui.NavigationLayout
import com.example.kalogatia.ui.theme.AppColorScheme
import com.example.kalogatia.viewmodels.RunExerciseViewModel
import com.example.kalogatia.viewmodels.SharedViewModel

@Composable
fun RunExerciseScreen(
    navController: NavController,
    onNavigate: (String) -> Unit,
    sharedViewModel: SharedViewModel,
    workoutId: Int,
) {
    val theme by sharedViewModel.currentTheme.collectAsState()
    val runExerciseViewModel: RunExerciseViewModel = viewModel()
    val timerValue by runExerciseViewModel.timer.collectAsState()

    LaunchedEffect(Unit) {
        runExerciseViewModel.startTimer(10)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        TopBarRunExerciseScreen(modifier = Modifier.weight(0.10f), theme, timerValue)
        RunExerciseScreenContent(modifier = Modifier.weight(0.80f), theme)
        NavigationLayout(modifier = Modifier.weight(0.10f), navController, onNavigate, theme)
    }
}

@Composable
fun TopBarRunExerciseScreen(modifier: Modifier, theme: AppColorScheme, timerValue: Int) {
    val isNegative = timerValue < 0
    val absTime = kotlin.math.abs(timerValue)

    val minutes = (absTime / 60).toString().padStart(2, '0')
    val seconds = (absTime % 60).toString().padStart(2, '0')

    val timerColor = if (isNegative) Color.Red else theme.textColor

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            Icons.Rounded.ArrowBackIosNew,
            contentDescription = "Back",
            tint = theme.iconColor,
            modifier = Modifier
                .clickable { }
                .weight(1f, false)
        )

        Text(
            text = if (isNegative) "- $minutes:$seconds" else "$minutes:$seconds",
            color = timerColor,
            modifier = Modifier.weight(2f, false)
        )

        tmpDoneButton(30.dp, 20.dp, modifier = Modifier.weight(1f, false))
    }
}





@Composable
fun RunExerciseScreenContent(modifier: Modifier, theme: AppColorScheme) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {

    }
}

@Composable
fun tmpDoneButton(circleSize: Dp, iconSize: Dp, modifier: Modifier) {
    Box(
        modifier = modifier
            .size(circleSize)
            .clip(CircleShape)
            .background(Color(0xFF0FFF50))
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "Done",
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}


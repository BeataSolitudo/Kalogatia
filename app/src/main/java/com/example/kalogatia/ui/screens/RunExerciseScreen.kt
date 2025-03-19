package com.example.kalogatia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.kalogatia.ui.NavigationLayout
import com.example.kalogatia.ui.theme.AppColorScheme
import com.example.kalogatia.viewmodels.SharedViewModel

@Composable
fun RunExerciseScreen(
    navController: NavController,
    onNavigate: (String) -> Unit,
    sharedViewModel: SharedViewModel,
    workoutId: Int
) {
    val theme by sharedViewModel.currentTheme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        TopBarRunExerciseScreen(modifier = Modifier.weight(0.15f), theme)
        RunExerciseScreenContent(modifier = Modifier.weight(0.75f), theme)
        NavigationLayout(modifier = Modifier.weight(0.10f), navController, onNavigate,theme)
    }
}

@Composable
fun TopBarRunExerciseScreen(modifier: Modifier, theme: AppColorScheme) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {

    }
}

@Composable
fun RunExerciseScreenContent(modifier: Modifier, theme: AppColorScheme) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {

    }
}
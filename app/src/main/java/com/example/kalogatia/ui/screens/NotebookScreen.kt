package com.example.kalogatia.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kalogatia.DataStoreManager
import com.example.kalogatia.ui.components.Divider
import com.example.kalogatia.ui.components.NavigationLayout
import com.example.kalogatia.ui.theme.AppColorScheme
import com.example.kalogatia.viewmodels.SharedViewModel
import kotlinx.coroutines.launch

@Composable
fun NoteBookScreen(
    navController: NavController,
    onNavigate: (String) -> Unit,
    sharedViewModel: SharedViewModel,
) {
    val theme by sharedViewModel.currentTheme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        // Top Bar - TodayWorkout, AddButton, StartButton
        TopBarNotebook(modifier = Modifier.weight(0.15f), theme)
        Divider(modifier = Modifier.background(theme.dividerColor))
        // Content - Workouts
        ContentNotebook(modifier = Modifier.weight(0.75f), theme)
        // Bottom Bar - Navigation
        NavigationLayout(modifier = Modifier.weight(0.10f), navController, onNavigate, theme)
    }
}

@Composable
fun TopBarNotebook(modifier: Modifier, theme: AppColorScheme) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Notebook" ,style = TextStyle(fontSize = 60.sp, fontWeight = FontWeight(800), color = theme.textColor))
    }
}

@Composable
fun ContentNotebook(modifier: Modifier, theme: AppColorScheme) {
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        DataStoreManager.getNotes(context).collect { myNotes ->
            if (myNotes != null) {
                text = myNotes
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(theme.backgroundColor)
            .padding(horizontal = 16.dp)
    ) {
        // Scrollable container
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Canvas(
                        modifier = Modifier.matchParentSize()
                    ) {
                        val lineSpacing = 28.0.dp.toPx() // lineHeight
                        val numberOfLines = (size.height / lineSpacing).toInt() + 10 // Extra lines to scroll

                        for (i in 1..numberOfLines) {
                            val y = i * lineSpacing
                            drawLine(
                                color = theme.dividerColor.copy(alpha = 0.5f),
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                    }

                    BasicTextField(
                        value = text,
                        onValueChange = { newText ->
                            text = newText
                            coroutineScope.launch {
                                DataStoreManager.saveNotes(context, newText)
                            }
                        },
                        textStyle = TextStyle(
                            color = theme.textColor,
                            fontSize = 22.sp,
                            lineHeight = 28.sp
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 30.dp) // Padding for first line
                    )
                }
            }
        }
    }
}


package com.example.kalogatia.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kalogatia.ui.Buttons.DoneButton
import com.example.kalogatia.ui.Buttons.RemoveButton
import com.example.kalogatia.ui.Divider
import com.example.kalogatia.ui.NavigationLayout

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddExerciseScreen(navController: NavController, onNavigate: (String) -> Unit) {
    val currentScreen = navController.currentBackStackEntry?.destination?.route ?: "Unknown"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E))
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        // Top Bar - TodayWorkout, AddButton, StartButton
        TopBarAddExerciseScreen(modifier = Modifier.weight(0.15f))
        //TopBarLayout(modifier = Modifier.weight(0.15f), workoutDao = null, currentScreen)
        Divider()
        // Content - Workouts
        ContentAddExercise(modifier = Modifier.weight(0.75f), )
        // Bottom Bar - Navigation
        NavigationLayout(modifier = Modifier.weight(0.10f), navController, onNavigate)
    }
}

@Composable
fun TopBarAddExerciseScreen(modifier: Modifier) {
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
                    .fillMaxSize()
                    .padding(start = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Add Exercise", fontSize = 40.sp, fontWeight = FontWeight(weight = 800), color = Color.White)
            }

            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxSize()
                    .padding(end = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                DoneButton()
            }
        }
    }
}

@Composable
fun ContentAddExercise(modifier: Modifier) {
    var name by remember { mutableStateOf("") }
    var restTime by remember { mutableStateOf("") }
    var setsList by remember { mutableStateOf(emptyList<SetData>()) }
    val exercisesList = remember { mutableStateListOf<ExerciseData>() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Exercise name", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(start = 20.dp))
        MyTextField(value = name, onValueChange = { name = it }, placeholderText = "Type Exercise Name")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Rest time", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(start = 20.dp))
        MyTextField(value = restTime, onValueChange = { restTime = it }, placeholderText = "Type Rest Time in seconds")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Sets", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(start = 20.dp))
        WorkoutSets()
    }
}

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText, color = Color(0xFFB3B3B3)) },
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color(0xFFB3B3B3)
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 4.dp)
            .background(Color(0xFF282639), shape = RoundedCornerShape(16.dp))
            .drawBehind {
                val strokeWidth = 4.dp.toPx()
                val offset = strokeWidth / 2
                val gradient = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF6A00F4), Color(0xFFF40072))
                )
                drawRoundRect(
                    brush = gradient,
                    size = size.copy(
                        width = size.width - strokeWidth,
                        height = size.height - strokeWidth
                    ),
                    topLeft = Offset(offset, offset),
                    cornerRadius = CornerRadius(16.dp.toPx()),
                    style = Stroke(strokeWidth)
                )
            }
    )
}

@Composable
fun WorkoutSets() {
    var setsList by remember { mutableStateOf(emptyList<SetData>()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFF282639), shape = RoundedCornerShape(16.dp))
            .border(
                width = 4.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF6A00F4), Color(0xFFF40072))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .sizeIn(minHeight = 50.dp)
            .padding(15.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) { Text(text = "SET", color = Color(0xFFB3B3B3)) }
            Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) { Text(text = "PREV", color = Color(0xFFB3B3B3)) }
            Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) { Text(text = "KG", color = Color(0xFFB3B3B3)) }
            Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) { Text(text = "REPS", color = Color(0xFFB3B3B3)) }
            Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) { Text(text = "RM", color = Color(0xFFB3B3B3)) }
        }

        setsList.forEachIndexed { index, set ->
            SpecificSet(
                order = (index + 1).toString(),
                prev = set.prev,
                weight = set.weight,
                reps = set.reps,
                onRemove = { setsList = setsList.filterIndexed { i, _ -> i != index } }
            )
        }

        AddSetButton {
            setsList = setsList + SetData(
                position = (setsList.size + 1).toString(),
                prev = "-",
                weight = "0 kg",
                reps = "0"
            )
        }
    }
}

@Composable
fun AddSetButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .background(Color(0xFF363352), shape = RoundedCornerShape(10.dp)).height(35.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "ADD SET", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SpecificSet(
    order: String,
    prev: String = "-",
    weight: String = "",
    reps: String = "",
    onRemove: () -> Unit
) {
    var weight by remember { mutableStateOf("") }
    var rep by remember { mutableStateOf("") }
    var isFocusedWeight by remember { mutableStateOf(false) }
    var isFocusedRep by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) { Text(text = order, color = Color.White) }
        Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) { Text(text = prev, color = Color.White) }

        Box(
            modifier = Modifier
                .weight(0.2f)
                .background(
                    if (isFocusedWeight) Color(0xFF3A3856) else Color(0xFF2E2C45),
                    shape = RoundedCornerShape(4.dp)
                )
                .width(60.dp)
                .height(36.dp)
                .padding(horizontal = 4.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = weight,
                onValueChange = { newWeight ->
                    // Validate input to allow up to 5 characters in total (e.g., "999.99")
                    if (newWeight.matches(Regex("^\\d{0,3}(\\.\\d{0,2})?$"))) {
                        weight = newWeight
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    color = Color.White
                ),
                cursorBrush = SolidColor(Color.White),
                modifier = Modifier
                    .fillMaxSize()
                    .onFocusChanged { isFocusedWeight = it.isFocused },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        innerTextField() // Draws the input text
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .weight(0.2f)
                .background(
                    if (isFocusedRep) Color(0xFF3A3856) else Color(0xFF2E2C45),
                    shape = RoundedCornerShape(4.dp)
                )
                .width(60.dp)
                .height(36.dp)
                .padding(horizontal = 4.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = rep,
                onValueChange = { newRep ->
                    if (newRep.matches(Regex("^\\d{0,4}$"))) { // Four digits only
                        rep = newRep
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    color = Color.White
                ),
                cursorBrush = SolidColor(Color.White),
                modifier = Modifier
                    .fillMaxSize()
                    .onFocusChanged { isFocusedRep = it.isFocused },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        innerTextField() // Draws the input text
                    }
                }
            )
        }

        Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) { RemoveButton(onRemove) }
    }
}


data class SetData(
    val position: String,
    val prev: String,
    val weight: String,
    val reps: String
)

data class ExerciseData(
    val exerciseName: String,
    val restTime: String,
    val sets: List<SetData>
)
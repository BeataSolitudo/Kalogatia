package com.example.kalogatia.ui.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kalogatia.data.entities.Exercise
import com.example.kalogatia.data.entities.Set
import com.example.kalogatia.ui.Buttons.DoneButton
import com.example.kalogatia.ui.Buttons.RemoveButton
import com.example.kalogatia.ui.NavigationLayout
import com.example.kalogatia.ui.NotFound
import com.example.kalogatia.ui.theme.AppColorScheme
import com.example.kalogatia.viewmodels.AddExerciseScreenViewModel
import com.example.kalogatia.viewmodels.RunExerciseViewModel
import com.example.kalogatia.viewmodels.SharedViewModel
import androidx.compose.runtime.key

@Composable
fun RunExerciseScreen(
    navController: NavController,
    onNavigate: (String) -> Unit,
    sharedViewModel: SharedViewModel,
    workoutId: Int,
) {
    val viewModel: RunExerciseViewModel = viewModel(factory = RunExerciseViewModel.provideFactory(workoutId))
    val theme by sharedViewModel.currentTheme.collectAsState()
    val timerValue by viewModel.timer.collectAsState()

    val exercises by viewModel.exercises.collectAsState()
    val setsMap by viewModel.setsMap.collectAsState()

    // Fetch exercises and sets when screen is launched
    LaunchedEffect(Unit) {
        viewModel.fetchExercises()
    }

    LaunchedEffect(Unit) {
        viewModel.fetchExerciseTypes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        TopBarRunExerciseScreen(modifier = Modifier.weight(0.10f), theme, timerValue, workoutId, navController, viewModel)
        RunExerciseScreenContent(modifier = Modifier.weight(0.80f), theme, exercises, setsMap, viewModel, navController)
        NavigationLayout(modifier = Modifier.weight(0.10f), navController, onNavigate, theme)
    }
}

@Composable
fun TopBarRunExerciseScreen(modifier: Modifier, theme: AppColorScheme, timerValue: Int, workoutId: Int, navController: NavController, viewModel: RunExerciseViewModel) {
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
                .clickable { navController.navigate("addWorkoutScreen/$workoutId") }
                .weight(1f, false)
        )

        Text(
            text = if (isNegative) "- $minutes:$seconds" else "$minutes:$seconds",
            color = timerColor,
            fontSize = 25.sp,
            modifier = Modifier.weight(2f, false)
        )

        tmpDoneButton(30.dp, 20.dp, modifier = Modifier.weight(1f, false), onDone = { viewModel.setClicked(true) })
    }
}

@Composable
fun RunExerciseScreenContent(
    modifier: Modifier,
    theme: AppColorScheme,
    exercises: List<Exercise>,
    setsMap: Map<Int, List<Set>>,
    viewModel: RunExerciseViewModel,
    navController: NavController
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (exercises.isEmpty()) {
                NotFound("You have no exercises!")
            } else {
                exercises.forEach { exercise ->
                    val exerciseSets = setsMap[exercise.exerciseId] ?: emptyList()
                    ExerciseWithSet(exercise, exerciseSets, theme, viewModel, navController)
                }
            }
        }
    }
}



@Composable
fun tmpDoneButton(circleSize: Dp, iconSize: Dp, modifier: Modifier, onDone: () -> Unit) {
    Box(
        modifier = modifier
            .size(circleSize)
            .clip(CircleShape)
            .background(Color(0xFF0FFF50))
            .clickable { onDone() },
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

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ExerciseWithSet(exercise: Exercise, sets: List<Set>, theme: AppColorScheme, viewModel: RunExerciseViewModel, navController: NavController) {
    val clicked by viewModel.click.collectAsState()
    val exerciseTypes = viewModel.exerciseTypes.collectAsState().value
    val exerciseTypeName = exerciseTypes.find { it.exerciseTypeId == exercise.exerciseTypeId }?.name
    var tmpSetList = remember { mutableStateListOf<RunSetData>() }
    var removedSets = remember { mutableStateListOf<Int>() }

    LaunchedEffect(sets) {
        if (sets.isNotEmpty()) {
            tmpSetList.clear() // Clear existing list
            tmpSetList.addAll(sets.mapIndexed { index, set ->
                RunSetData(
                    id = set.setId,
                    position = index + 1,
                    prev = "-",
                    weight = set.weight,
                    reps = set.repetition,
                    done = false
                )
            })
        }
    }

    LaunchedEffect(clicked) {
        if(clicked) {
            viewModel.setClicked(false)
            // Remove set
            if (!removedSets.isEmpty()) {
                println("Sets removed")
                viewModel.deleteSets(removedSets)
            }
            // Insert Set if id = null
            tmpSetList.forEach { setData ->
                if (setData.id == null && exercise.exerciseId != null) {
                    println("Inserted Set")
                    viewModel.insertSet(setData.position, setData.weight, setData.reps, exercise.exerciseId)
                }
            }
            // Update sets
            tmpSetList.forEach { setData ->
                if (setData.id != null) {
                    val fetchedSet = sets.find { it.setId == setData.id }
                    if (fetchedSet != null) {
                        if (fetchedSet.weight != setData.weight || fetchedSet.repetition != setData.reps) {
                            println("Updated set")
                            viewModel.updateSet(setData.id, setData.position, setData.weight, setData.reps)
                        }
                    }
                }
            }

        }
    }

    LaunchedEffect(clicked) {
        if (clicked) {
            viewModel.setClicked(false)
            navController.navigate("mainScreen/")
            println("Navigate")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(theme.textFieldBackgroundColor, shape = RoundedCornerShape(16.dp))
            .drawBehind {
                val strokeWidth = 4.dp.toPx()
                val offset = strokeWidth / 2
                val gradient = Brush.horizontalGradient(
                    colors = listOf(theme.borderColorGradient, theme.borderColorGradient2)
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
            .sizeIn(minHeight = 50.dp)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        exerciseTypeName?.let { Text(text = it, color = theme.textColor, fontSize = 25.sp) }

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
            Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) { Text(text = "CHECK", color = Color(0xFFB3B3B3)) }
        }

        tmpSetList.forEachIndexed { index, set ->
            key(set) {
                RunSet(
                    order = (index + 1).toString(),
                    prev = "-",
                    weightF = set.weight.toString(),
                    reps = set.reps.toString(),
                    onWeightChange = { newWeight ->
                        tmpSetList[index] = tmpSetList[index].copy(weight = newWeight.toDoubleOrNull() ?: 0.0)
                    },
                    onRepsChange = { newReps ->
                        tmpSetList[index] = tmpSetList[index].copy(reps = newReps.toIntOrNull() ?: 0)
                    },
                    onRemove = {
                        tmpSetList.removeAt(index)
                        if (set.id != null) {
                            removedSets.add(set.id)
                        }
                        println(removedSets) },
                    onDone = {
                        tmpSetList[index] = tmpSetList[index].copy(done = !tmpSetList[index].done)
                        if (tmpSetList[index].done == true) { viewModel.resetTimer(); viewModel.startTimer(exercise.restTime) }

                    },
                    theme = theme,
                    isDone = set.done
                )

            }
        }

        Box(modifier = Modifier.padding(top = 10.dp)) {
            AddSetButton(theme) {
                tmpSetList.add(
                    RunSetData(
                        id = null,
                        position = tmpSetList.size + 1,
                        prev = "-",
                        weight = 0.0,
                        reps = 0,
                        done = false
                    )
                )
            }
        }

    }
}

@Composable
fun RunSet(
    order: String,
    prev: String = "-",
    weightF: String,
    reps: String,
    onWeightChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onRemove: () -> Unit,
    onDone: () -> Unit,
    theme: AppColorScheme,
    isDone: Boolean
) {
    var weight by rememberSaveable { mutableStateOf(weightF) }
    var rep by rememberSaveable { mutableStateOf(reps) }
    var isWeightFocused by remember { mutableStateOf(false) }
    var isRepsFocused by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val rowBackgroundColor = if (isDone) Color(0xFF228B22).copy(alpha = 0.5f) else theme.textFieldBackgroundColor
    val textFieldBackgroundColor = if (isDone) Color(0xFF228B22).copy(alpha = 0.5f) else theme.clickableTextFieldColor

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(rowBackgroundColor)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            Box(modifier = Modifier
                .weight(0.2f)
                .clickable {
                    expanded = !expanded
                }, contentAlignment = Alignment.Center) {
                Text(text = order, color = theme.textColor)
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
                            onRemove()
                        },
                        modifier = Modifier.height(25.dp)
                    )
                }
            }
            Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) {
                Text(text = prev, color = theme.textColor)
            }

            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .background(
                        textFieldBackgroundColor,
                        shape = RoundedCornerShape(4.dp)
                    ) // Updated
                    .width(60.dp)
                    .height(36.dp)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = weight,
                    onValueChange = { newWeight ->
                        if (newWeight.matches(Regex("^\\d{0,3}(\\.\\d{0,2})?$"))) {
                            weight = newWeight
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onWeightChange(weight)
                            focusManager.clearFocus()
                        }
                    ),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        color = theme.textColor
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier
                        .fillMaxSize()
                        .onFocusChanged {
                            if (!it.isFocused && isWeightFocused) {
                                onWeightChange(weight)
                            }
                            isWeightFocused = it.isFocused
                        }
                )
            }

            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .background(
                        textFieldBackgroundColor,
                        shape = RoundedCornerShape(4.dp)
                    ) // Updated
                    .width(60.dp)
                    .height(36.dp)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = rep,
                    onValueChange = { newRep ->
                        if (newRep.matches(Regex("^\\d{0,4}$"))) {
                            rep = newRep
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onRepsChange(rep)
                            focusManager.clearFocus()
                        }
                    ),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        color = theme.textColor
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier
                        .fillMaxSize()
                        .onFocusChanged {
                            if (!it.isFocused && isRepsFocused) {
                                onRepsChange(rep)
                            }
                            isRepsFocused = it.isFocused
                        }
                )
            }

            Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) {
                tmpDoneButton(24.dp, 16.dp, Modifier, onDone)
            }
        }
    }
}




data class RunSetData(
    val id: Int?,
    val position: Int,
    val prev: String,
    val weight: Double,
    val reps: Int,
    var done: Boolean = false
)
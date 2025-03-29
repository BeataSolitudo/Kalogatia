package com.example.kalogatia.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kalogatia.data.entities.Set
import com.example.kalogatia.ui.Buttons.DoneButton
import com.example.kalogatia.ui.Buttons.RemoveButton
import com.example.kalogatia.ui.components.Divider
import com.example.kalogatia.ui.components.NavigationLayout
import com.example.kalogatia.ui.theme.AppColorScheme
import com.example.kalogatia.viewmodels.AddExerciseScreenViewModel
import com.example.kalogatia.viewmodels.SharedViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddExerciseScreen(
    navController: NavController,
    onNavigate: (String) -> Unit,
    exerciseId: Int?,
    workoutId: Int?,
    sharedViewModel: SharedViewModel
) {
    val viewModel: AddExerciseScreenViewModel = viewModel(factory = AddExerciseScreenViewModel.provideFactory(exerciseId))
    val theme by sharedViewModel.currentTheme.collectAsState()

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            if ( backStackEntry.destination.route != "addExerciseScreen/{exerciseId}" && backStackEntry.destination.route != "addWorkoutScreen/{workoutId}") {
                sharedViewModel.saveTmpWorkoutName(null)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        // Top Bar - TodayWorkout, AddButton, StartButton
        TopBarAddExerciseScreen(modifier = Modifier.weight(0.15f), theme, viewModel)
        Divider(modifier = Modifier.background(theme.dividerColor))
        // Content - Workouts
        ContentAddExercise(modifier = Modifier.weight(0.75f), viewModel, exerciseId, theme, workoutId, navController)
        // Bottom Bar - Navigation
        NavigationLayout(modifier = Modifier.weight(0.10f), navController, onNavigate, theme)
    }
}

@Composable
fun TopBarAddExerciseScreen(modifier: Modifier, theme: AppColorScheme, viewModel: AddExerciseScreenViewModel) {
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
                Text(text = "Add Exercise", fontSize = 40.sp, fontWeight = FontWeight(weight = 800), color = theme.textColor)
            }

            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxSize()
                    .padding(end = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                DoneButton(onClick = { viewModel.setClicked(true) })
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ContentAddExercise(modifier: Modifier, viewModel: AddExerciseScreenViewModel, exerciseId: Int?, theme: AppColorScheme, workoutId: Int?, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var tmpName by remember { mutableStateOf("") }
    var restTime by remember { mutableStateOf("") }
    var tmpRestTime by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var tmpDay by remember { mutableStateOf("") }
    val fetchedExerciseWithType by viewModel.exercisesWithType.collectAsState()
    val clicked by viewModel.click.collectAsState()
    var emptyName by remember { mutableStateOf(false) }
    var emptyRestTime by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val fetchedWeekDay by viewModel.weekDay.collectAsState()
    val fetchedSets by viewModel.sets.collectAsState()
    val weekDays = mapOf(
        0 to "None",
        1 to "Mon", 2 to "Tue", 3 to "Wed", 4 to "Thu",
        5 to "Fri", 6 to "Sat", 7 to "Sun"
    )

    LaunchedEffect(exerciseId) {
        if (exerciseId != null) {
            viewModel.fetchExerciseAccessories(exerciseId)
            viewModel.fetchSets(exerciseId)
        } else {
            name = ""
            restTime = ""
            day = ""
        }
    }

    LaunchedEffect(exerciseId) {
        exerciseId?.let { viewModel.fetchExercise(it) }
    }

    LaunchedEffect(fetchedExerciseWithType?.exercise?.workoutId) {
        fetchedExerciseWithType?.exercise?.let {
            viewModel.fetchWorkoutDay(it.workoutId)
        }
    }

    LaunchedEffect(fetchedExerciseWithType, fetchedWeekDay) {
        fetchedExerciseWithType?.let {
            name = it.exerciseTypeName
            tmpName = it.exerciseTypeName
            restTime = it.exercise.restTime.toString()
            tmpRestTime = it.exercise.restTime.toString()
        }

        fetchedWeekDay?.let {
            val dayName = weekDays[it] ?: "Unknown"
            day = dayName
            tmpDay = dayName
        }
    }

    LaunchedEffect(clicked) {
        if (viewModel.click.value == true) {
            viewModel.setClicked(false)

            if (name.isEmpty()) {
                emptyName = true
            } else if (restTime.isEmpty()) {
                emptyRestTime = true
            } else {
                emptyName = false
                emptyRestTime = false

                // If rest time exists update rest time
                if (!restTime.equals(tmpRestTime) && exerciseId != null) {
                    println("Changed rest time")
                    viewModel.updateRestTime(exerciseId, restTime.toInt())
                    navController.navigate("addWorkoutScreen/${workoutId}")
                }
                // If exerciseName changes (already exists) updates exercise name
                if (!name.equals(tmpName) && exerciseId != null) {
                    println("Changed exercise name")
                    viewModel.exercise.value?.let { viewModel.updateExerciseType(name, it.exerciseTypeId) }
                    navController.navigate("addWorkoutScreen/${workoutId}")
                }
                // If user creating new exercise:
                if (exerciseId == null) {
                    viewModel.insertExerciseType(name)
                    viewModel.fetchExerciseTypeId(name) { id ->
                        if (id != null && workoutId != null) {
                            viewModel.insertExercise(id, restTime.toInt(), workoutId)
                            navController.navigate("addWorkoutScreen/${workoutId}")
                        }
                    }
                }
                // Updating workout day
                if (workoutId != null && !tmpDay.isEmpty() && !day.equals("None")) {
                    if (!day.isEmpty() && !day.equals(tmpDay)) {
                        println("Update day")
                        val selectedDayInt = weekDays.entries.find { it.value == day }?.key
                        val oldDay = weekDays.entries.find { it.value == tmpDay }?.key
                        if (selectedDayInt != null && oldDay != null && workoutId != null) {
                        viewModel.updateWeekDay(workoutId, oldDay, selectedDayInt)
                        navController.navigate("addWorkoutScreen/${workoutId}")
                        }
                    }
                }
                // Inserting workout day
                if (!day.isEmpty() && !day.equals("None") && viewModel.weekDay.value == null) {
                    println("workoutPlanningId fetched"+viewModel.weekDay.value)
                    println("Insert day")
                    val selectedDayInt = weekDays.entries.find { it.value == day }?.key
                    if (selectedDayInt != null && workoutId != null)
                    viewModel.insertWorkoutDay(workoutId, 1, selectedDayInt)
                    navController.navigate("addWorkoutScreen/${workoutId}")
                }
                // If workout day equals none remove
                val selectedDayInt = weekDays.entries.find { it.value == tmpDay }?.key
                if (day.equals("None") && workoutId != null && selectedDayInt != null) {
                    println("Removed day")
                    viewModel.deleteWorkoutPlanning(workoutId, selectedDayInt)
                    navController.navigate("addWorkoutScreen/${workoutId}")
                }
            }

        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Exercise name", color = theme.textColor, fontSize = 20.sp, modifier = Modifier.padding(start = 20.dp))
        MyTextField(value = name, onValueChange = { name = it }, placeholderText = "Type exercise name", theme, emptyName)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier.weight(0.5f)) {
                val regex = "^[1-9][0-9]*$".toRegex()   // Only whole positive numbers

                Column {
                    Text(
                        text = "Rest time",
                        color = theme.textColor,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                    MyTextField(
                        value = restTime,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.matches(regex)) {
                                restTime = newValue
                            }
                        },
                        placeholderText = "Rest Time in s",
                        theme,
                        emptyRestTime
                    )
                }
            }
            Box(modifier = Modifier.weight(0.5f)) {
                Column {
                    Text(
                        text = "Workout day",
                        color = theme.textColor,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                    MyTextField(
                        value = day,
                        onValueChange = { day = it },
                        placeholderText = "Workout Day",
                        theme,
                        false,
                        focusable = false,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        weekDays.forEach { (_, option) ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    day = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Sets", color = theme.textColor, fontSize = 20.sp, modifier = Modifier.padding(start = 20.dp))
        WorkoutSets(fetchedSets, theme, viewModel, navController, workoutId, exerciseId)
    }
}

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    theme: AppColorScheme,
    isError: Boolean? = false,
    focusable: Boolean? = true,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    focusable?.let {
        TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        enabled = it,
        placeholder = { Text(placeholderText, color = Color(0xFFB3B3B3)) },
        textStyle = TextStyle(color = theme.textColor, fontSize = 18.sp),
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
            .drawBehind {
                drawRoundRect(
                    color = theme.textFieldBackgroundColor,
                    size = size,
                    topLeft = Offset.Zero,
                    cornerRadius = CornerRadius(16.dp.toPx())
                )

                val strokeWidth = 4.dp.toPx()
                val offset = strokeWidth / 2
                val gradient = Brush.horizontalGradient(
                    colors = if (isError == true) {
                        listOf(Color(0xFFEF0107), Color(0xFFDE3163))
                    } else {
                        listOf(theme.borderColorGradient, theme.borderColorGradient2)
                    }// listOf(theme.borderColorGradient, theme.borderColorGradient2)
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
}

@Composable
fun WorkoutSets(fetchedSets: List<Set>?, theme: AppColorScheme, viewModel: AddExerciseScreenViewModel, navController: NavController, workoutId: Int?, exerciseId: Int?) {
    val clicked by viewModel.click.collectAsState()
    var tmpSetList = remember { mutableStateListOf<SetData>() }
    var removedSets = remember { mutableStateListOf<Int>() }

    LaunchedEffect(fetchedSets) {
        if (!fetchedSets.isNullOrEmpty()) {
            tmpSetList.clear() // Clear existing list
            tmpSetList.addAll(fetchedSets.mapIndexed { index, set ->
                SetData(
                    id = set.setId,
                    position = index + 1,
                    prev = "-",
                    weight = set.weight,
                    reps = set.repetition
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
                navController.navigate("addWorkoutScreen/${workoutId}")
            }
            // Insert Set if id = null
            tmpSetList.forEach { setData ->
                if (setData.id == null && exerciseId != null) {
                    println("Inserted Set")
                    viewModel.insertSet(setData.position, setData.weight, setData.reps, exerciseId)
                }
            }
            // Update sets
            tmpSetList.forEach { setData ->
                if (setData.id != null) {
                    val fetchedSet = fetchedSets?.find { it.setId == setData.id }
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

        tmpSetList.forEachIndexed { index, setData ->
            key(setData) {
                SpecificSet(
                    order = (index + 1).toString(),
                    prev = setData.prev,
                    weightF = setData.weight.toString(),
                    reps = setData.reps.toString(),
                    onWeightChange = { newWeight ->
                        tmpSetList[index] = tmpSetList[index].copy(weight = newWeight.toDoubleOrNull() ?: 0.0)
                    },
                    onRepsChange = { newReps ->
                        tmpSetList[index] = tmpSetList[index].copy(reps = newReps.toIntOrNull() ?: 0)
                    },
                    onRemove = {
                        tmpSetList.removeAt(index)
                        if (setData.id != null) {
                            removedSets.add(setData.id)
                        }
                        println(removedSets)
                    },
                    theme
                )
            }
        }


        AddSetButton(theme) {
            tmpSetList.add(
                SetData(
                    id = null,
                    position = tmpSetList.size + 1,
                    prev = "-",
                    weight = 0.0,
                    reps = 0
                )
            )
        }
    }
}

@Composable
fun AddSetButton(
    theme: AppColorScheme,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .background(theme.customButtonColor, shape = RoundedCornerShape(10.dp))
            .height(35.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "ADD SET", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SpecificSet(
    order: String,
    prev: String = "-",
    weightF: String,
    reps: String,
    onWeightChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onRemove: () -> Unit,
    theme: AppColorScheme
) {
    var weight by rememberSaveable { mutableStateOf(weightF) }
    var rep by rememberSaveable { mutableStateOf(reps) }
    var isWeightFocused by remember { mutableStateOf(false) }
    var isRepsFocused by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) {
            Text(text = order, color = theme.textColor)
        }
        Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) {
            Text(text = prev, color = theme.textColor)
        }

        Box(
            modifier = Modifier
                .weight(0.2f)
                .background(theme.backgroundColor, shape = RoundedCornerShape(4.dp))
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

        // Vstup pro opakování
        Box(
            modifier = Modifier
                .weight(0.2f)
                .background(theme.backgroundColor, shape = RoundedCornerShape(4.dp))
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
                        onRepsChange(rep)  // Aktualizace seznamu po stisku Enter
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
                            onRepsChange(rep)  // Aktualizace seznamu po ztrátě fokusu
                        }
                        isRepsFocused = it.isFocused
                    }
            )
        }

        Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) {
            RemoveButton(onRemove)
        }
    }
}



data class SetData(
    val id: Int?,
    val position: Int,
    val prev: String,
    val weight: Double,
    val reps: Int
)
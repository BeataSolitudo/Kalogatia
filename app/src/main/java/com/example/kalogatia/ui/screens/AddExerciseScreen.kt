package com.example.kalogatia.ui.screens

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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.kalogatia.ui.Divider
import com.example.kalogatia.ui.NavigationLayout
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

@Composable
fun ContentAddExercise(modifier: Modifier, viewModel: AddExerciseScreenViewModel, exerciseId: Int?, theme: AppColorScheme, workoutId: Int?, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var restTime by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    val fetchedExerciseWithType by viewModel.exercisesWithType.collectAsState()
    val clicked by viewModel.click.collectAsState()
    var emptyName by remember { mutableStateOf(false) }
    var emptyRestTime by remember { mutableStateOf(false) }
    val fetchedWeekDay by viewModel.weekDay.collectAsState()
    val fetchedSets by viewModel.sets.collectAsState()
    val weekDays = mapOf(
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

    LaunchedEffect(fetchedExerciseWithType?.exercise?.workoutId) {
        fetchedExerciseWithType?.exercise?.let {
            viewModel.fetchWorkoutDay(it.workoutId)
        }
    }

    LaunchedEffect(fetchedExerciseWithType, fetchedWeekDay) {
        fetchedExerciseWithType?.let {
            name = it.exerciseTypeName
            restTime = it.exercise.restTime.toString()
        }

        fetchedWeekDay?.let {
            val dayName = weekDays[it] ?: "Unknown"
            day = dayName
        }
    }

    LaunchedEffect(clicked) {
        if (viewModel.click.value == true) {
            viewModel.setClicked(false)

            if (exerciseId == null) {
                if (name.isEmpty()) {
                    emptyName = true
                } else if (restTime.isEmpty()) {
                    emptyRestTime = true
                } else {
                    viewModel.insertExerciseType(name)
                    viewModel.fetchExerciseTypeId(name) { id ->
                        if (id != null && workoutId != null) {
                            viewModel.insertExercise(id, restTime.toInt(), workoutId)
                            navController.navigate("addWorkoutScreen/${workoutId}")
                        }
                    }
                    emptyName = false
                    emptyRestTime = false
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
                Column {
                    Text(
                        text = "Rest time",
                        color = theme.textColor,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                    MyTextField(
                        value = restTime,
                        onValueChange = { restTime = it },
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
                        false
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Sets", color = theme.textColor, fontSize = 20.sp, modifier = Modifier.padding(start = 20.dp))
        WorkoutSets(fetchedSets, theme, viewModel)
    }
}

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    theme: AppColorScheme,
    isError: Boolean? = false,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

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

@Composable
fun WorkoutSets(fetchedSets: List<Set>?, theme: AppColorScheme, viewModel: AddExerciseScreenViewModel) {
    val clicked by viewModel.click.collectAsState()
    var tmpSetList by remember { mutableStateOf(mutableListOf<SetData>()) }

    // ✅ Initialize tmpSetList when fetchedSets is loaded
    LaunchedEffect(fetchedSets) {
        if (!fetchedSets.isNullOrEmpty()) {
            tmpSetList = fetchedSets.mapIndexed { index, set ->
                SetData(
                    id = set.setId,
                    position = index + 1,
                    prev = "-",
                    weight = "${set.weight} Kg",
                    reps = set.repetition.toString()
                )
            }.toMutableList()
        }
    }

    // ✅ Compare lists when "clicked" changes
    LaunchedEffect(clicked) {
        if (clicked) {
            val fetchedSetList = fetchedSets?.map { set ->
                set.setId to SetData(
                    id = set.setId,
                    position = 0, // Position is ignored for comparison
                    prev = "-",
                    weight = "${set.weight} Kg",
                    reps = set.repetition.toString()
                )
            }?.toMap() ?: emptyMap() // Convert to map for quick lookup

            val tmpSetMap = tmpSetList.associateBy { it.id } // Create map for quick lookup

            // 1️⃣ Check for Removed Sets (Exists in fetchedSets but not in tmpSetList)
            for ((setId, setData) in fetchedSetList) {
                if (setId != null && !tmpSetMap.containsKey(setId)) {
                    println("Removed Set ID: $setId")
                }
            }

            // 2️⃣ Check for Added Sets (No setId in tmpSetList)
            for (set in tmpSetList) {
                if (set.id == null) {
                    println("Added Set: $set")
                }
            }

            // 3️⃣ Check for Modified Sets (Exists in both but has changes)
            for ((setId, setData) in fetchedSetList) {
                if (setId != null && tmpSetMap.containsKey(setId)) {
                    val tmpSet = tmpSetMap[setId]
                    if (tmpSet != null && (setData.weight != tmpSet.weight || setData.reps != tmpSet.reps)) {
                        println("Modified Set: $tmpSet")
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

        // ✅ Render tmpSetList (Fetched + New Sets)
        tmpSetList.forEachIndexed { index, setData ->
            SpecificSet(
                order = (index + 1).toString(),
                prev = setData.prev,
                weightF = setData.weight,
                reps = setData.reps,
                onRemove = {
                    tmpSetList = tmpSetList.filterIndexed { i, _ -> i != index }
                        .mapIndexed { newIndex, set -> set.copy(position = newIndex + 1) }
                        .toMutableList()
                },
                theme
            )
        }

        // ✅ Add new set correctly
        AddSetButton(theme) {
            tmpSetList = (tmpSetList + SetData(
                id = null,
                position = (tmpSetList.size + 1),
                prev = "-",
                weight = "0 Kg",
                reps = "0"
            )).toMutableList()
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
    weightF: String = "",
    reps: String = "",
    onRemove: () -> Unit,
    theme: AppColorScheme
) {
    var weight by remember { mutableStateOf("") }
    var rep by remember { mutableStateOf("") }
    var isFocusedWeight by remember { mutableStateOf(false) }
    var isFocusedRep by remember { mutableStateOf(false) }

    LaunchedEffect(order) {
        rep = reps
        weight = weightF
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) { Text(text = order, color = theme.textColor) }
        Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) { Text(text = prev, color = theme.textColor) }

        Box(
            modifier = Modifier
                .weight(0.2f)
                .background(
                    if (isFocusedWeight) theme.borderColorGradient else theme.backgroundColor,
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
                    color = theme.textColor
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
                    if (isFocusedRep) theme.borderColorGradient else theme.backgroundColor,
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
                    color = theme.textColor
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
    val id: Int?,
    val position: Int,
    val prev: String,
    val weight: String,
    val reps: String
)
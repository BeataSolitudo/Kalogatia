package com.example.kalogatia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.kalogatia.ui.components.Divider
import com.example.kalogatia.ui.components.NavigationLayout
import com.example.kalogatia.ui.theme.AppColorScheme
import com.example.kalogatia.viewmodels.GraphScreenViewModel
import com.example.kalogatia.viewmodels.SharedViewModel

@Composable
fun GraphScreen(
    navController: NavController,
    onNavigate: (String) -> Unit,
    sharedViewModel: SharedViewModel,
) {
    val theme by sharedViewModel.currentTheme.collectAsState()
    val viewModel: GraphScreenViewModel = viewModel(factory = GraphScreenViewModel.provideFactory())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.backgroundColor)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        TopBarGraphScreen(modifier = Modifier.weight(0.15f), theme)
        Divider(modifier = Modifier.background(theme.dividerColor))
        GraphScreenContent(modifier = Modifier.weight(0.75f), theme, viewModel)
        NavigationLayout(modifier = Modifier.weight(0.10f), navController, onNavigate, theme)
    }
}

@Composable
fun TopBarGraphScreen(modifier: Modifier, theme: AppColorScheme) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Graph" ,style = TextStyle(fontSize = 60.sp, fontWeight = FontWeight(800), color = theme.textColor))
    }
}

@Composable
fun GraphScreenContent(modifier: Modifier, theme: AppColorScheme, viewModel: GraphScreenViewModel) {
    val sets by viewModel.sets.collectAsState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        contentAlignment = Alignment.TopStart
    ) {
        val steps = 6

        val noData: List<Point> = listOf(
            Point(0f, 0f),
            Point(0f, 0f),
        )

        var pointsData by remember { mutableStateOf(noData) }

        LaunchedEffect(sets) {
            println("You selected set")
            println(sets)
            pointsData = sets.mapIndexedNotNull { index, set ->
                if (set.max_weight != null && set.year != null) {
                    val y = set.max_weight.toFloat()
                    Point(index.toFloat(), y)
                } else {
                    null
                }
            }

            if (pointsData.isEmpty()) {
                pointsData = noData
            }

            println(pointsData)
        }

        // Move creation of lineChartData inside a derivedStateOf block
        val lineChartData by remember(pointsData) {
            derivedStateOf {
                val xAxisData = AxisData.Builder()
                    .axisStepSize(100.dp)
                    .backgroundColor(Color.Transparent)
                    .steps(pointsData.size - 1)
                    .labelData { i -> if (sets.size > i) (sets[i].week_of_year + " - " + sets[i].year) else ""}
                    .labelAndAxisLinePadding(15.dp)
                    .axisLineColor(theme.selectedNavigationItemColor)
                    .axisLabelColor(theme.selectedNavigationItemColor)
                    .build()

                val yAxisData = AxisData.Builder()
                    .steps(steps)
                    .backgroundColor(theme.backgroundColor)
                    .labelAndAxisLinePadding(20.dp)
                    .labelData { i ->
                        val yScale = 100 / steps
                        (i * yScale).toString()
                    }
                    .axisLineColor(theme.selectedNavigationItemColor)
                    .axisLabelColor(theme.selectedNavigationItemColor)
                    .build()

                LineChartData(
                    linePlotData = LinePlotData(
                        lines = listOf(
                            Line(
                                dataPoints = pointsData,
                                LineStyle(
                                    color = theme.textColor,    // Color of graph line
                                    lineType = LineType.SmoothCurve(isDotted = false),
                                ),
                                IntersectionPoint(color = theme.textColor),
                                SelectionHighlightPoint(color = theme.selectedNavigationItemColor),
                                ShadowUnderLine(
                                    alpha = 0.5f,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            theme.selectedNavigationItemColor,
                                            Color.Transparent
                                        )
                                    )
                                ),
                                SelectionHighlightPopUp()
                            )
                        ),
                    ),
                    xAxisData = xAxisData,
                    yAxisData = yAxisData,
                    gridLines = GridLines(),
                    backgroundColor = theme.backgroundColor
                )
            }
        }

        var selectedTab by remember { mutableStateOf(0) }

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            SearchableDropdownExample(theme, viewModel)

            TimeRangePicker(
                selectedIndex = selectedTab,
                onSelectionChange = { selectedTab = it },
                viewModel
            )

            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(theme.backgroundColor),
                lineChartData = lineChartData
            )
        }
    }
}

@Composable
fun TimeRangePicker(
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit,
    viewModel: GraphScreenViewModel
) {
    val options = listOf("1M", "6M", "1Y", "Max")

    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = Modifier.fillMaxWidth(),
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedIndex])
            )
        }
    ) {
        options.forEachIndexed { index, text ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onSelectionChange(index) },
                text = { Text(text)},
            )
        }
    }
}

@Composable
fun SearchableDropdown(
    items: List<String>,
    onItemSelected: (String) -> Unit,
    theme: AppColorScheme
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = items.filter { it.contains(searchQuery, ignoreCase = true) }

    Column {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
            },
            textStyle = TextStyle(color = theme.textColor)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filteredItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        searchQuery = item
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SearchableDropdownExample(theme: AppColorScheme, viewModel: GraphScreenViewModel) {
    val exerciseTypes by viewModel.exerciseTypes.collectAsState()
    var selectedItem by remember { mutableStateOf("") }
    var selectedExerciseTypeId by remember { mutableStateOf<Int?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        SearchableDropdown(
            items = exerciseTypes.map { it.name },
            onItemSelected = { selectedName ->
                selectedItem = selectedName
                selectedExerciseTypeId = exerciseTypes.find { it.name == selectedName }?.exerciseTypeId
                selectedExerciseTypeId?.let { viewModel.fetchHistorySetsByExerciseType(it) } // Call function with ID
            },
            theme = theme
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selected: $selectedItem", color = theme.textColor)
    }
}

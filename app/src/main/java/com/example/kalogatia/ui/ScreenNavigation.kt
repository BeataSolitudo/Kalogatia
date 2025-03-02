package com.example.kalogatia.ui

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

sealed class ScreenNavigation(
    val id: Int,
    val title: String,
    @SuppressLint("SupportAnnotationUsage") @DrawableRes val selectedIconId: ImageVector,
    @SuppressLint("SupportAnnotationUsage") @DrawableRes val unSelectedIconId: ImageVector,
    val route: String
) {
    object Home : ScreenNavigation(id = 0, title ="Home", selectedIconId = Icons.Rounded.Home, unSelectedIconId = Icons.Rounded.Home, route = "mainScreen/")
    object Calendar : ScreenNavigation(id = 1, title ="Calendar", selectedIconId = Icons.Rounded.CalendarMonth, unSelectedIconId = Icons.Rounded.CalendarMonth, route = "")
    object Add : ScreenNavigation(id = 1, title ="Add", selectedIconId = Icons.Rounded.Add, unSelectedIconId = Icons.Rounded.Add, route = "addWorkoutScreen/{workoutId}")
    object Graph : ScreenNavigation(id = 1, title ="Graph", selectedIconId = Icons.Rounded.AutoGraph, unSelectedIconId = Icons.Rounded.AutoGraph, route = "")
    object Settings : ScreenNavigation(id = 1, title ="Settings", selectedIconId = Icons.Rounded.Settings, unSelectedIconId = Icons.Rounded.Settings, route = "")
}

@Composable
fun Navigation(navController: NavController, onNavigate: (String) -> Unit) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        NavItem(item = ScreenNavigation.Home, isSelected = (currentRoute == ScreenNavigation.Home.route), onNavigate)
        NavItem(item = ScreenNavigation.Graph, isSelected = (currentRoute == ScreenNavigation.Graph.route), onNavigate)
        NavItem(item = ScreenNavigation.Add, isSelected = (currentRoute == ScreenNavigation.Add.route || currentRoute == "addExerciseScreen/{exerciseId}"), onNavigate)
        NavItem(item = ScreenNavigation.Calendar, isSelected = (currentRoute == ScreenNavigation.Calendar.route), onNavigate)
        NavItem(item = ScreenNavigation.Settings, isSelected = (currentRoute == ScreenNavigation.Settings.route), onNavigate)
    }
}

@Composable
fun NavItem(item: ScreenNavigation, isSelected: Boolean, onNavigate: (String) -> Unit) {
    val iconId = if (isSelected) item.selectedIconId else item.unSelectedIconId
    val iconAlpha = if (isSelected) 1f else 0.5f
    IconButton(onClick = { onNavigate(item.route) }) {
        Icon(
            imageVector = iconId,
            contentDescription = item.title,
            tint = if (isSelected) {
                Color(0xFFFF4081).copy(alpha = iconAlpha)
            } else {
                Color.Black.copy(alpha = iconAlpha)
            },
            modifier = Modifier.size(30.dp)
        )
    }
}

fun handleNavigation(navController: NavController, route: String) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    if (currentRoute == "addWorkoutScreen/{workoutId}" && route == "addWorkoutScreen/{workoutId}") {
        navController.navigate("addExerciseScreen/{exerciseId}")
    } else {
        navController.navigate(route)
    }
}

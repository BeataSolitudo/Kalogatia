package com.example.kalogatia.ui

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.ContentPaste
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kalogatia.ui.theme.AppColorScheme

sealed class ScreenNavigation(
    val id: Int,
    val title: String,
    @SuppressLint("SupportAnnotationUsage") @DrawableRes val selectedIconId: ImageVector,
    @SuppressLint("SupportAnnotationUsage") @DrawableRes val unSelectedIconId: ImageVector,
    val route: String
) {
    object Home : ScreenNavigation(id = 0, title ="Home", selectedIconId = Icons.Rounded.Home, unSelectedIconId = Icons.Rounded.Home, route = "mainScreen/")
    object Calendar : ScreenNavigation(id = 1, title ="Notebook", selectedIconId = Icons.Rounded.ContentPaste, unSelectedIconId = Icons.Rounded.ContentPaste, route = "notebook/")
    object Add : ScreenNavigation(id = 1, title ="Add", selectedIconId = Icons.Rounded.Add, unSelectedIconId = Icons.Rounded.Add, route = "addWorkoutScreen/{workoutId}")
    object Graph : ScreenNavigation(id = 1, title ="Graph", selectedIconId = Icons.Rounded.AutoGraph, unSelectedIconId = Icons.Rounded.AutoGraph, route = "graphScreen/")
    object Settings : ScreenNavigation(id = 1, title ="Settings", selectedIconId = Icons.Rounded.Settings, unSelectedIconId = Icons.Rounded.Settings, route = "settingsScreen/")
}

@Composable
fun Navigation(navController: NavController, onNavigate: (String) -> Unit, theme: AppColorScheme, workoutId: Int? = null) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        NavItem(item = ScreenNavigation.Home, isSelected = (currentRoute == ScreenNavigation.Home.route), onNavigate, theme)
        NavItem(item = ScreenNavigation.Graph, isSelected = (currentRoute == ScreenNavigation.Graph.route), onNavigate, theme)
        NavItem(item = ScreenNavigation.Add, isSelected = (currentRoute == ScreenNavigation.Add.route), onNavigate, theme)
        NavItem(item = ScreenNavigation.Calendar, isSelected = (currentRoute == ScreenNavigation.Calendar.route), onNavigate, theme)
        NavItem(item = ScreenNavigation.Settings, isSelected = (currentRoute == ScreenNavigation.Settings.route), onNavigate, theme)
    }
}

@Composable
fun NavItem(item: ScreenNavigation, isSelected: Boolean, onNavigate: (String) -> Unit, theme: AppColorScheme) {
    val iconId = if (isSelected) item.selectedIconId else item.unSelectedIconId
    val iconAlpha = if (isSelected) 1f else 0.5f
    IconButton(onClick = { onNavigate(item.route) }) {
        Icon(
            imageVector = iconId,
            contentDescription = item.title,
            tint = if (isSelected) {
                theme.selectedNavigationItemColor.copy(alpha = iconAlpha)
            } else {
                Color.Black.copy(alpha = iconAlpha)
            },
            modifier = Modifier.size(30.dp)
        )
    }
}

fun handleNavigation(navController: NavController, route: String) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    if (!(currentRoute == "addWorkoutScreen/{workoutId}" && route == "addWorkoutScreen/{workoutId}")) {
        navController.navigate(route)
    }
}

@Composable
fun NavigationLayout(modifier: Modifier = Modifier, navController: NavController, onNavigate: (String) -> Unit, theme: AppColorScheme, workoutId: Int? = null) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(theme.navigationColor),
        contentAlignment = Alignment.Center
    ) {
        Navigation(navController, onNavigate, theme, workoutId)
    }
}
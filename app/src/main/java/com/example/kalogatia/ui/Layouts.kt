package com.example.kalogatia.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun NavigationLayout(modifier: Modifier = Modifier, navController: NavController, onNavigate: (String) -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(Color(0xFF25283C)),
        contentAlignment = Alignment.Center
    ) {
        Navigation(navController, onNavigate)
    }
}

@Composable
fun Divider() {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val topPadding = screenHeight / 22

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(2.dp)
                .background(Color.White)
        )
    }

    Spacer(modifier = Modifier.height(topPadding.dp))
}




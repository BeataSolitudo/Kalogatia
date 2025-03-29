package com.example.kalogatia.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kalogatia.R

@Composable
fun NotFound(errorMessage: String) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val topPadding = screenHeight / 6

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, top = topPadding.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.dumbbell_colored),
                contentDescription = "Dumbbell Icon"
            )
            Text(
                text = errorMessage,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
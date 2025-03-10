package com.example.kalogatia.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun Divider(modifier: Modifier) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val topPadding = screenHeight / 22

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth(0.85f)
                .height(2.dp)
        )
    }

    Spacer(modifier = Modifier.height(topPadding.dp))
}




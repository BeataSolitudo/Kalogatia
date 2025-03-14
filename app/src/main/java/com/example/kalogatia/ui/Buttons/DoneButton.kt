package com.example.kalogatia.ui.Buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DoneButton(onClick: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .size(55.dp)
            .clip(CircleShape)
            .background(Color(0xFF0FFF50))
            .clickable { onClick(true) },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "Done",
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}

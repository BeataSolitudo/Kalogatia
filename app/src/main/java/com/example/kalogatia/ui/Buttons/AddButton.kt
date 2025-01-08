package com.example.kalogatia.ui.Buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AddButton() {
    Box(
        modifier = Modifier
            .size(55.dp)
            .shadow(17.dp, shape = CircleShape, ambientColor = Color(0xFF000000), spotColor = Color(0xFF000000))
            .background(color = Color(0xFF57bff3), shape = CircleShape)
            .clickable {

            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.background(Color.Black).height(30.dp).width(4.dp)
        )
        Box(
            modifier = Modifier.background(Color.Black).height(4.dp).width(30.dp)
        )
    }
}
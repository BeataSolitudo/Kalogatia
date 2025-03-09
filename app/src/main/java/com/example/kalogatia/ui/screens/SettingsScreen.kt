package com.example.kalogatia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdUnits
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kalogatia.ui.Divider
import com.example.kalogatia.ui.NavigationLayout

@Composable
fun SettingsScreen(
    navController: NavController,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E))
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        TopBarSettingsScreen(modifier = Modifier.weight(0.15f))
        Divider()
        SettingsScreenContent(modifier = Modifier.weight(0.75f))
        NavigationLayout(modifier = Modifier.weight(0.10f), navController, onNavigate)
    }
}

@Composable
fun TopBarSettingsScreen(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Settings" ,style = TextStyle(fontSize = 60.sp, fontWeight = FontWeight(800), color = Color.White))
    }
}

@Composable
fun SettingsScreenContent(modifier: Modifier) {
    val spacerDp = 15.dp
    val themeFontSize = 20.sp

    var emailDialog = remember { mutableStateOf(false) }
    var aboutDialog = remember { mutableStateOf(false) }

    if (emailDialog.value) {
        AlertDialogExample(dialogTitle = "Our Mail", dialogText = "j.kalivoda.st@spseiostrava.cz", icon = Icons.Rounded.Mail, onDismissRequest =  { emailDialog.value = false })
    }
    if (aboutDialog.value) {
        AlertDialogExample(dialogTitle = "About", dialogText = "Kalogatia was my first project, created to help me and my friends track our progress more efficiently. Many apps offered either an amazing UI or great functionality, but unfortunately, not both. If an app had a great UI, it was often overwhelming, cluttered with distractions, and difficult to navigate. On the other hand, apps with excellent functionality often lacked a polished user interface. That's where Kalogatia comes in combining the best of both worlds!", icon = Icons.Rounded.AdUnits, onDismissRequest =  { aboutDialog.value = false })
    }

    Box(
        modifier = modifier.fillMaxWidth().padding(30.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Leave your thoughts", color = Color.White, fontSize = 23.sp)
            myDivider(modifier = Modifier.fillMaxWidth(1f))
            Row(
                modifier = Modifier.clickable { emailDialog.value = true },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Create,
                    contentDescription = "writeIcon",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp).padding(end = 5.dp)
                )
                Text(text = "Write us an email", color = Color.White, fontSize = 20.sp, modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = "writeIcon",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp).padding(end = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(spacerDp))

            Text(text = "About", color = Color.White, fontSize = 23.sp)
            myDivider(modifier = Modifier.fillMaxWidth(1f))
            Row(
                modifier = Modifier.clickable { aboutDialog.value = true },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Rounded.QuestionMark,
                    contentDescription = "writeIcon",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp).padding(end = 5.dp)
                )
                Text(text = "About app", color = Color.White, fontSize = 20.sp, modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = "writeIcon",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp).padding(end = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(spacerDp))

            Text(text = "Theme", color = Color.White, fontSize = 23.sp)
            myDivider(modifier = Modifier.fillMaxWidth(1f).padding(bottom = 10.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1c1c1e))
                    .height(70.dp)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(0.3f)
                            .fillMaxHeight(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Dark", color = Color.White, fontSize = themeFontSize)
                    }
                    Box(
                        modifier = Modifier
                            .weight(0.3f)
                            .fillMaxHeight(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "White", color = Color.White, fontSize = themeFontSize)
                    }
                    Box(
                        modifier = Modifier
                            .weight(0.3f)
                            .fillMaxHeight(1f)
                            .clip(RoundedCornerShape(16.dp)).background(Color.Red),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Colorful", color = Color.White, fontSize = themeFontSize)
                    }
                }
            }


        }
    }
}

@Composable
fun myDivider(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.85f)
            .height(2.dp)
            .background(Color.Gray)
    )
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(
                text = dialogText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth())
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {

        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Close")
            }
        }
    )
}

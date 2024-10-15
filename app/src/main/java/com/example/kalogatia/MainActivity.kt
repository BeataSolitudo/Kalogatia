package com.example.kalogatia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kalogatia.ui.theme.AddWorkoutScreen
import com.example.kalogatia.ui.theme.KalogatiaTheme
import com.example.kalogatia.ui.theme.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KalogatiaTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "mainScreen", builder =  {
                    composable("mainScreen") {
                        MainScreen({ navController.navigate("addWorkoutScreen") })
                    }
                    composable("addWorkoutScreen") {
                        AddWorkoutScreen()
                    }
                })
            }
        }
    }
}




package com.example.newsrep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.newsrep.navigation.NavGraph
import com.example.newsrep.ui.theme.NewsRepTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsRepTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
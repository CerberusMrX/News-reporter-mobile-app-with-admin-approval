package com.example.newsrep.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsrep.screens.LoginScreen
import com.example.newsrep.screens.admin.AdminDashboard
import com.example.newsrep.screens.reporter.ReporterDashboard
import com.example.newsrep.screens.user.UserDashboard

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToAdmin = { navController.navigate("admin") },
                onNavigateToReporter = { navController.navigate("reporter") },
                onNavigateToUser = { navController.navigate("user") }
            )
        }
        
        composable("admin") {
            AdminDashboard(
                onBackClick = { navController.navigateUp() }
            )
        }
        
        composable("reporter") {
            ReporterDashboard(
                onBackClick = { navController.navigateUp() }
            )
        }
        
        composable("user") {
            UserDashboard(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
} 
package com.example.newsrep.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object AdminDashboard : Screen("admin_dashboard")
    object ReporterDashboard : Screen("reporter_dashboard")
    object UserDashboard : Screen("user_dashboard")
} 
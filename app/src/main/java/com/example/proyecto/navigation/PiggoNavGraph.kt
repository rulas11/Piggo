package com.example.proyecto.navigation
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto.features.dashboard.ConfigurationScreen
import com.example.proyecto.features.dashboard.DashboardScreen
import com.example.proyecto.features.dashboard.PiggoViewModel

@Composable
fun PiggoNavGraph(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            val piggoViewModel: PiggoViewModel = viewModel()
            DashboardScreen(
                viewModel = piggoViewModel,
                navController = navController
            )
        }

        composable("configuration") {
            ConfigurationScreen(
                onBackClick = { navController.popBackStack() },
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeChange
            )
        }
    }
}
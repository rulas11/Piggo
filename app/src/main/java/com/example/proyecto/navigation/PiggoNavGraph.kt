package com.example.proyecto.navigation
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto.ui.ConfigurationScreen
import com.example.proyecto.features.dashboard.DashboardScreen
import com.example.proyecto.features.dashboard.PiggoViewModel
import com.example.proyecto.features.goals.GoalsScreen
import com.example.proyecto.features.tips.TipsScreen
import com.example.proyecto.features.analisis.AnalisisScreen
import com.example.proyecto.features.transactions.TransactionsScreen

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

        composable("analisis") {
            AnalisisScreen(
                navController = navController
            )
        }

        composable("goals") {
            GoalsScreen(
                navController = navController
            )
        }

        composable("transactions") {
            TransactionsScreen(
                navController = navController
            )
        }

        composable("tips") {
            TipsScreen(
                navController = navController
            )
        }

    }
}
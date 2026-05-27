package com.example.proyecto.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto.PiggoApplication
import com.example.proyecto.ui.ConfigurationScreen
import com.example.proyecto.features.dashboard.DashboardScreen
import com.example.proyecto.features.goals.GoalsScreen
import com.example.proyecto.features.tips.TipsScreen
import com.example.proyecto.features.analisis.AnalisisScreen
import com.example.proyecto.features.dashboard.DashboardViewModel
import com.example.proyecto.features.transactions.TransactionsScreen
import com.example.proyecto.features.transactions.TransactionsViewModel

@Composable
fun PiggoNavGraph(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val repository = (context.applicationContext as PiggoApplication).repository

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            val viewModel: DashboardViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return DashboardViewModel(repository) as T
                    }
                }
            )
            DashboardScreen(
                viewModel = viewModel,
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

        composable(route = "transactions") {
            val viewModel: TransactionsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return TransactionsViewModel(repository) as T
                    }
                }
            )

            TransactionsScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("tips") {
            TipsScreen(
                navController = navController
            )
        }

    }
}
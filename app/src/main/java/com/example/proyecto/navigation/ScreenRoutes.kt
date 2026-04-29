package com.example.proyecto.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Dashboard : Screen(
        route = "dashboard",
        title = "Inicio",
        icon = Icons.Default.Home
    )

    object Transactions : Screen(
        route = "transactions",
        title = "Flujo",
        icon = Icons.Default.List
    )

    object Goals : Screen(
        route = "goals",
        title = "Metas",
        icon = Icons.Default.Star
    )

    object Tips : Screen(
        route = "tips",
        title = "Tips",
        icon = Icons.Default.Search
    )

    object Analisis : Screen(
        route = "analisis",
        title = "Analisis",
        icon = Icons.Default.Info
    )
}

val bottomNavItems = listOf(
    Screen.Analisis,
    Screen.Goals,
    Screen.Dashboard,
    Screen.Transactions,
    Screen.Tips
)
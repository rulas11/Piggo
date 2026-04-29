package com.example.proyecto.ui
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyecto.navigation.bottomNavItems

@Composable
fun PiggoBottomBar(navController: NavHostController) {
    val colorAzulMarino = Color(0xFF1A346C)
    val colorVerdeMenta = Color(0xFF92E8BF)
    val colorBlanco = Color.White
    val colorGrisInactivo = Color(0xFF8A9AAB)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = colorAzulMarino,
        contentColor = colorBlanco
    ) {
        bottomNavItems.forEach { screen ->
            val isSelected = currentRoute == screen.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = "Ir a ${screen.title}"
                    )
                },
                label = {
                    Text(text = screen.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorAzulMarino,
                    selectedTextColor = colorVerdeMenta,
                    indicatorColor = colorVerdeMenta,
                    unselectedIconColor = colorGrisInactivo,
                    unselectedTextColor = colorGrisInactivo
                )
            )
        }
    }
}
package com.example.proyecto.features.tips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyecto.ui.PiggoBottomBar
import com.example.proyecto.ui.theme.MontserratFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsScreen(
    navController: NavHostController
) {

    val colorAzulMarino = Color(0xFF0058A2)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "PIGGO", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = {navController.navigate("configuration")  }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Opciones")
                    }
                }
            )
        },
        bottomBar = { PiggoBottomBar(navController = navController) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Prueba",
                fontSize = 18.sp,
                fontFamily = MontserratFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                PiggoCircularChart(
                    title = "Prueba",
                    progress = 0.40f,
                    color = colorAzulMarino,
                )
            }
        }
    }
}

@Composable
fun PiggoCircularChart(
    title: String,
    progress: Float,
    color: Color,
    titleColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(130.dp)
    ) {
        CircularProgressIndicator(
            progress = 1f,
            modifier = Modifier.fillMaxSize(),
            color = Color.LightGray.copy(alpha = 0.3f),
            strokeWidth = 14.dp
        )

        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxSize(),
            color = color,
            strokeWidth = 14.dp,
        )

        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
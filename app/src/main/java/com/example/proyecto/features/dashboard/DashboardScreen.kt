package com.example.proyecto.features.dashboard
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyecto.ui.PiggoBottomBar
import com.example.proyecto.ui.PiggoMascot
import com.example.proyecto.ui.theme.MontserratFamily
import com.example.proyecto.ui.theme.TextLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: PiggoViewModel,
    navController: NavHostController
) {
    val state by viewModel.piggoState.collectAsState()

    val colorAzulMarino = Color(0xFF0058A2)
    val colorVerdeMenta = Color(0xFF92E8BF)

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

            Box(modifier = Modifier.size(200.dp)) {
                PiggoMascot(state = state)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Balance: $12,500.00",
                fontSize = 24.sp,
                fontFamily = MontserratFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Actualmente piggy está: Estable",
                fontSize = 18.sp,
                fontFamily = MontserratFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Metas:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                PiggoCircularChart(
                    title = "Ingresos",
                    progress = 0.75f,
                    color = colorVerdeMenta,
                )

                PiggoCircularChart(
                    title = "Gastos",
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
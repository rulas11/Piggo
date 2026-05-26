package com.example.proyecto.features.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyecto.data.local.TransactionEntity
import com.example.proyecto.ui.PiggoBottomBar
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    navController: NavHostController,
    transactions: List<TransactionEntity>
) {
    var selectedTab by remember { mutableIntStateOf(3) }
    val tabs = listOf("Hoy", "Semana", "Mes", "Todos")
    val viewModel: Transaction = viewModel()
    var transactionToEdit by remember { mutableStateOf<Transaction?>(null) }


    val filteredTransactions = remember(selectedTab, transactions) {
        val now = Calendar.getInstance()
        transactions.filter { tx ->
            val txCal = Calendar.getInstance().apply { time = tx.dateMillis }
            when (selectedTab) {
                0 -> isSameDay(now, txCal)
                1 -> isSameWeek(now, txCal)
                2 -> isSameMonth(now, txCal)
                else -> true
            }
        }.sortedByDescending { it.dateMillis }
    }

    val groupedTransactions = filteredTransactions.groupBy {
        SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(it.dateMillis)
    }

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
            Spacer(modifier = Modifier.width(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Movimientos", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = Color(0xFF1A346C)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(text = title, fontSize = 12.sp) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                groupedTransactions.forEach { (date, txs) ->
                    item {
                        Text(
                            text = date.replaceFirstChar { it.uppercase() },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(txs) { tx ->

                    }
                }

                if (filteredTransactions.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "No hay movimientos en este periodo", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

private fun isSameWeek(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)
}

private fun isSameMonth(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
}

package com.example.proyecto.features.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyecto.TransactionType
import com.example.proyecto.data.local.TransactionEntity
import com.example.proyecto.features.QuickAddBottomSheet
import com.example.proyecto.ui.PiggoBottomBar
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    navController: NavHostController,
    viewModel: TransactionsViewModel
) {
    val allTransactions by viewModel.transactions.collectAsState()

    var selectedTimeTab by remember { mutableIntStateOf(3) } // 3 = "Todos"
    val timeTabs = listOf("Hoy", "Semana", "Mes", "Todos")

    var selectedCategory by remember { mutableStateOf("Todas") }
    val categories = listOf("Todas", "Ingreso_Fijo", "Ingreso_Variable", "Alimentacion", "Transporte", "Entretenimiento", "Vivienda", "Salud", "Educacion","Servicios","Otros")

    var transactionToEdit by remember { mutableStateOf<TransactionEntity?>(null) }

    val filteredTransactions = remember(selectedTimeTab, selectedCategory, allTransactions) {
        val now = Calendar.getInstance()

        allTransactions.filter { tx ->
            val txCal = Calendar.getInstance().apply { time = tx.dateMillis }

            val pasesTimeFilter = when (selectedTimeTab) {
                0 -> isSameDay(now, txCal)
                1 -> isSameWeek(now, txCal)
                2 -> isSameMonth(now, txCal)
                else -> true
            }

            val pasesCategoryFilter = if (selectedCategory == "Todas") {
                true
            } else {
                tx.category.name.uppercase() == selectedCategory.uppercase()
            }

            pasesTimeFilter && pasesCategoryFilter
        }.sortedByDescending { it.dateMillis }
    }

    val groupedTransactions = filteredTransactions.groupBy {
        SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(it.dateMillis)    }

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
                .background(Color(0xFF13172E))
                .padding(paddingValues)
        ) {
            Text(
                text = "Movimientos",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(24.dp)
            )

            TabRow(
                selectedTabIndex = selectedTimeTab,
                containerColor = Color.Transparent,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTimeTab]),
                        color = Color(0xFF3B5998)
                    )
                }
            ) {
                timeTabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTimeTab == index,
                        onClick = { selectedTimeTab = index },
                        text = { Text(title) }
                    )
                }
            }

            LazyRow(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF92E8BF),
                            selectedLabelColor = Color.Black
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                if (filteredTransactions.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay movimientos con estos filtros", color = Color.Gray)
                        }
                    }
                } else {
                    groupedTransactions.forEach { (dateStr, txs) ->
                        item {
                            Text(
                                text = dateStr.replaceFirstChar { it.uppercase() },
                                color = Color.LightGray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                        items(txs) { tx ->
                            TransactionItemCard(
                                tx = tx,
                                onEditClick = { transactionToEdit = tx },
                                onDeleteClick = { viewModel.deleteTransaction(tx) })
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }

        if (transactionToEdit != null) {
            QuickAddBottomSheet(
                isExpense = transactionToEdit!!.isIncome == TransactionType.GASTO,
                initialTransaction = transactionToEdit,
                onDismiss = { transactionToEdit = null },
                onSave = { amount, desc, type, cat, date ->
                    viewModel.updateTransaction(
                        id = transactionToEdit!!.id,
                        amount = amount,
                        desc = desc,
                        isIncome = type,
                        category = cat,
                        date = date
                    )
                    transactionToEdit = null
                }
            )
        }
    }
}

@Composable
fun TransactionItemCard(
    tx: TransactionEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = tx.description.toString(),
                    color = Color(0xFF1A346C),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = tx.category.name.toString(),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (tx.isIncome == TransactionType.INGRESO) "+$${tx.amount}" else "-$${tx.amount}",
                    color = if (tx.isIncome == TransactionType.INGRESO) Color(0xFF00C853) else Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                IconButton(onClick = {
                    showMenu = true
                }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Opciones", tint = Color.Gray)
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            showMenu = false
                            onEditClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar", color = Color.Red) },
                        onClick = {
                            showMenu = false
                            onDeleteClick()
                        }
                    )
                }
            }
        }
    }
}

fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
fun isSameWeek(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)
}
fun isSameMonth(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
}
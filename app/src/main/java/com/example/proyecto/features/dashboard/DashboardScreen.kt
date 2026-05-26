package com.example.proyecto.features.dashboard


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyecto.FinancialState
import com.example.proyecto.features.transactions.QuickAddBottomSheet
import com.example.proyecto.features.transactions.Transaction
import com.example.proyecto.ui.PiggoBottomBar
import com.example.proyecto.ui.PiggoMascot
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    navController: NavHostController,
) {
    val uiState: DashboardState by viewModel.uiState.collectAsState()
    var showQuickAdd by remember { mutableStateOf(false) }
    var isAddingExpense by remember { mutableStateOf(true) }
    var transactionToEdit by remember { mutableStateOf<Transaction?>(null) }

    val colorAzulMarino = Color(0xFF1A346C)
    val colorVerdeMenta = Color(0xFF92E8BF)

    val currentMonth = SimpleDateFormat("MMMM", Locale("es", "MX")).format(Date()).replaceFirstChar { it.uppercase() }

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Balance del mes: $${String.format(Locale.US, "%.2f", uiState.balance)}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorAzulMarino
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.size(180.dp)) {
                    PiggoMascot(state = uiState.financialState)
                }

                Text(
                    text = getStatusPhrase(uiState.financialState),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorAzulMarino
                )
                Text(
                    text = "\"${getHumorousPhrase(uiState.financialState)}\"",
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ActionButton(
                        text = "Agregar gasto",
                        color = Color(0xFFE57373),
                        modifier = Modifier.weight(1f)
                    ) { 
                        isAddingExpense = true
                        showQuickAdd = true 
                    }
                    ActionButton(
                        text = "Agregar ingreso",
                        color = colorVerdeMenta,
                        modifier = Modifier.weight(1f)
                    ) { 
                        isAddingExpense = false
                        showQuickAdd = true 
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Resumen de $currentMonth",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorAzulMarino,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                        AnimatedPieChart(categories = uiState.categories, totalIncome = uiState.totalIncome)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        val maxVisibleLabels = 11
                        val visibleCategories = uiState.categories.take(maxVisibleLabels)
                        
                        visibleCategories.forEach { cat ->
                            CategoryLabel(cat)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                            Box(modifier = Modifier.size(10.dp).background(Color.LightGray.copy(alpha = 0.5f), CircleShape))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Dinero libre: $${uiState.balance.toInt()}",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

            }

        }

        if (showQuickAdd || (transactionToEdit != null)) {
            QuickAddBottomSheet(
                isExpense = isAddingExpense,
                initialTransaction = transactionToEdit,
                onDismiss = { 
                    showQuickAdd = false
                    transactionToEdit = null
                },
                onSave = { amount, desc, type, cat, isGoal, date ->
                    if (transactionToEdit != null) {
                        viewModel.updateTransaction(
                            id = transactionToEdit!!.id,
                            amount = amount,
                            desc = desc,
                            type = type,
                            category = cat,
                            isGoal = isGoal,
                            dateMillis = date
                        )
                    } else {
                        viewModel.addTransaction(
                            amount = amount,
                            desc = desc,
                            type = type,
                            category = cat,
                            isGoal = isGoal,
                            dateMillis = date
                        )
                    }
                    showQuickAdd = false
                    transactionToEdit = null
                }
            )
        }

    }
}

@Composable
fun ActionButton(text: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(4.dp)
    ) {
        Text(text = text, color = if (color == Color(0xFF92E8BF)) Color(0xFF1A346C) else Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AnimatedPieChart(categories: List<CategoryBudget>, totalIncome: Double) {
    val totalSpent = categories.sumOf { it.spent }
    val incomeToUse = totalIncome.coerceAtLeast(totalSpent)

    Canvas(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        var startAngle = -90f

        drawCircle(
            color = Color.Black.copy(alpha = 0.1f),
            radius = size.minDimension / 2,
            center = center + Offset(4.dp.toPx(), 4.dp.toPx())
        )

        categories.forEach { cat ->
            val sweepAngle = (cat.spent / incomeToUse).toFloat() * 360f
            if (sweepAngle > 0) {
                drawArc(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            cat.color.copy(alpha = 0.8f),
                            cat.color),
                        center = center,
                        radius = size.minDimension / 2
                    ),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = Size(size.width, size.height)
                )
                drawArc(
                    color = Color.White.copy(alpha = 0.2f),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
            startAngle += sweepAngle
        }

        if (incomeToUse > totalSpent) {
            val freeSweep = ((incomeToUse - totalSpent) / incomeToUse).toFloat() * 360f
            drawArc(
                color = Color.LightGray.copy(alpha = 0.3f),
                startAngle = startAngle,
                sweepAngle = freeSweep,
                useCenter = true
            )
        }
    }
}

@Composable
fun CategoryLabel(cat: CategoryBudget) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Box(modifier = Modifier.size(10.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${cat.category.name.replace("_", " ")}: $${cat.spent.toInt()}/$${cat.budget.toInt()}",
            fontSize = 11.sp,
            color = if (cat.spent > cat.budget) Color.Red else Color(0xFF4CAF50),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onEdit: (Transaction) -> Unit,
    onDelete: (Transaction) -> Unit
) {
    val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color(0xFF1A346C)
                )
                Text(
                    text = transaction.description,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = sdf.format(transaction.date),
                    fontSize = 11.sp,
                    color = Color.LightGray
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (transaction.type.name.contains("INGRESO")) "+" else "-"}$${String.format(Locale.US, "%.2f", transaction.amount)}",
                    color = if (transaction.type.name.contains("INGRESO")) Color(0xFF4CAF50) else Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Opciones", tint = Color.Gray)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            onClick = {
                                showMenu = false
                                onEdit(transaction)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar", color = Color.Red) },
                            onClick = {
                                showMenu = false
                                onDelete(transaction)
                            }
                        )
                    }
                }
            }
        }
    }
}

fun getStatusPhrase(state: FinancialState): String {
    return when(state) {
        FinancialState.PERFECT -> "PIGGO está tan feliz"
        FinancialState.GOOD -> "PIGGO está muy bien"
        FinancialState.STABLE -> "PIGGO está estable"
        FinancialState.WARNING -> "PIGGO está preocupado"
        FinancialState.CRITICAL -> "PIGGO está en pánico"
    }
}

fun getHumorousPhrase(state: FinancialState): String {
    return when(state) {
        FinancialState.PERFECT -> "Ya estoy viendo terrenos en Marte. Con lo que vas ahorrando, compramos la Luna el martes."
        FinancialState.GOOD -> "Siente el ritmo del ahorro, nene. No es suerte, es puro talento financiero."
        FinancialState.STABLE -> "¿Gastar o no gastar? Esa es la cuestión. Estamos a un 'OXXO' de la tragedia."
        FinancialState.WARNING -> "Si yo no veo el estado de cuenta, el estado de cuenta no me ve a mí... ¿verdad?"
        FinancialState.CRITICAL -> "¡Tadá! Hice desaparecer todo el dinero. El truco salió perfecto, lástima que no hay para la cena."
    }
}



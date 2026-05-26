package com.example.proyecto.features.dashboard

import android.R
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.alpha
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto.data.local.TransactionEntity
import com.example.proyecto.data.repository.TransactionRepository
import com.example.proyecto.features.transactions.Category
import com.example.proyecto.features.transactions.TransactionType
import com.example.proyecto.ui.theme.Purple80
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

data class CategoryBudget(
    val category: Category,
    val spent: Double,
    val budget: Double,
    val color: Color
)

class DashboardViewModel(
    private val repository: TransactionRepository,
) : ViewModel() {

    val uiState: StateFlow<DashboardState> = repository.allTransactions
        .map { transactionsList ->

            val ingresosTotales = transactionsList
                .filter { it.isIncome == TransactionType.INGRESO }
                .sumOf { it.amount }


            val gastosPorCategoria = transactionsList
                .filter { it.isIncome == TransactionType.GASTO }
                .groupBy { it.category }
                .map { (nombreCategoria, listaDeGastos) ->
                    val totalGastadoEnEstaCategoria = listaDeGastos.sumOf { it.amount }

                    CategoryBudget(
                        category = Category.valueOf(nombreCategoria.toString()),
                        spent = totalGastadoEnEstaCategoria,
                        budget = 1000.0,
                        color = obtenerColorPorCategoria(nombreCategoria.toString())
                    )
                }

            DashboardState(
                recentTransactions = transactionsList,
                totalIncome = ingresosTotales,
                categories = gastosPorCategoria
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardState()
        )

    fun addTransaction(
        amount: Double,
        desc: String,
        type: TransactionType,
        category: Category,
        isGoal: Boolean,
        dateMillis: Date
    ) {
        viewModelScope.launch {
            val newTransaction = TransactionEntity(
                amount = amount,
                description = desc,
                isIncome = type,
                category = category,
                isGoal = isGoal,
                dateMillis = dateMillis
            )
            repository.addTransaction(newTransaction)
        }
    }

    fun updateTransaction(
        id: Int,
        amount: Double,
        desc: String,
        type: TransactionType,
        category: Category,
        isGoal: Boolean,
        dateMillis: Date
    ) {
        viewModelScope.launch {
            val updatedTransaction = TransactionEntity(
                amount = amount,
                description = desc,
                isIncome = type,
                category = category,
                isGoal = isGoal,
                dateMillis = dateMillis
            )
            repository.addTransaction(updatedTransaction)
        }
    }
}

fun obtenerColorPorCategoria(categoria: String): Color {
    val azulMarino = Color(0xFF1A346C)
    val verdeMenta = Color(0xFF92E8BF)
    val rosaPastel = Color(0xFFFFC4D1)
    val amarilloMostaza = Color(0xFFFFD166)
    val lilaSuave = Color(0xFFB19CD9)


    return when (categoria.uppercase()) {


        "VIVIENDA" -> verdeMenta

        "SALUD" -> amarilloMostaza

        "ALIMENTACION" -> azulMarino

        "OTROS" -> lilaSuave

        else -> rosaPastel
    }
}
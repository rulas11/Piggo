package com.example.proyecto.features.dashboard

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto.FinancialState
import com.example.proyecto.data.local.TransactionEntity
import com.example.proyecto.data.repository.TransactionRepository
import com.example.proyecto.Category
import com.example.proyecto.TransactionType
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

            val gastosTotales = gastosPorCategoria.sumOf { it.spent }

            val estadoActual = calcularEstadoFinanciero(ingresosTotales, gastosTotales)


            DashboardState(
                recentTransactions = transactionsList,
                totalIncome = ingresosTotales,
                categories = gastosPorCategoria,
                financialState = estadoActual
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
        dateMillis: Date
    ) {
        viewModelScope.launch {
            val newTransaction = TransactionEntity(
                amount = amount,
                description = desc,
                isIncome = type,
                category = category,
                dateMillis = dateMillis
            )
            repository.addTransaction(newTransaction)
        }
    }
}

fun obtenerColorPorCategoria(categoria: String): Color {
    val azulMarino = Color(0xFF1A346C)
    val verdeMenta = Color(0xFF92E8BF)
    val rosaPastel = Color(0xFFFFC4D1)
    val amarilloMostaza = Color(0xFFFFD166)
    val lilaSuave = Color(0xFFB19CD9)
    val rojo = Color(0xFF600000)
    val azulClaro = Color(0xFF006BEF)
    val naranja = Color(0xFFB26400)
    val verde = Color(0xFF597A00)

    return when (categoria.uppercase()) {


        "VIVIENDA" -> verdeMenta

        "SALUD" -> amarilloMostaza

        "EDUCACION" -> rojo

        "ALIMENTACION" -> verde

        "TRANSPORTE" -> azulMarino

        "SERVICIOS" -> azulClaro

        "ENTRETENIMIENTO" -> naranja

        "OTROS" -> lilaSuave

        else -> rosaPastel
    }
}

private fun calcularEstadoFinanciero(ingresos: Double, gastos: Double): FinancialState {
    if (ingresos <= 0.0) {
        return if (gastos > 0) FinancialState.CRITICAL else FinancialState.STABLE
    }

    val porcentajeGastado = (gastos / ingresos) * 100

    return when {
        porcentajeGastado <= 25.0 -> FinancialState.PERFECT
        porcentajeGastado <= 50.0 -> FinancialState.GOOD
        porcentajeGastado <= 75.0 -> FinancialState.STABLE
        porcentajeGastado < 100.0 -> FinancialState.WARNING
        else -> FinancialState.CRITICAL
    }
}
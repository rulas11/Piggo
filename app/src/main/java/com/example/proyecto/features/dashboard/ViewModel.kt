package com.example.proyecto.features.dashboard

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.proyecto.FinancialState
import com.example.proyecto.model.Category
import com.example.proyecto.model.Transaction
import com.example.proyecto.model.TransactionType
import com.example.proyecto.model.PaymentMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import java.util.UUID

data class CategoryBudget(
    val category: Category,
    val spent: Double,
    val budget: Double,
    val color: Color
)

data class DashboardState(
    val financialState: FinancialState = FinancialState.PERFECT,
    val totalIncome: Double = 15000.0,
    val categories: List<CategoryBudget> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList()
) {
    val totalExpenses: Double get() = categories.sumOf { it.spent }
    val balance: Double get() = totalIncome - totalExpenses
}

class PiggoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardState())
    val uiState = _uiState.asStateFlow()

    private val _piggoState = MutableStateFlow(FinancialState.PERFECT)
    val piggoState = _piggoState.asStateFlow()

    init {
        val initialCategories = listOf(
            CategoryBudget(Category.ALIMENTACION, 500.0, 3000.0, Color(0xFFFF5252)),
            CategoryBudget(Category.TRANSPORTE, 1200.0, 1000.0, Color(0xFFFFAB40)),
            CategoryBudget(Category.ENTRETENIMIENTO, 800.0, 1500.0, Color(0xFF448AFF)),
            CategoryBudget(Category.OTROS, 500.0, 1000.0, Color(0xFF7C4DFF))
        )
        
        val initialTransactions = listOf(
            Transaction(UUID.randomUUID().toString(), 500.0, "Súper del fin", Date(), TransactionType.GASTO, Category.ALIMENTACION, PaymentMethod.EFECTIVO),
            Transaction(UUID.randomUUID().toString(), 1200.0, "Gasolina mes", Date(), TransactionType.GASTO, Category.TRANSPORTE, PaymentMethod.TARJETA_DEBITO),
            Transaction(UUID.randomUUID().toString(), 800.0, "Cine y cena", Date(), TransactionType.GASTO, Category.ENTRETENIMIENTO, PaymentMethod.EFECTIVO),
            Transaction(UUID.randomUUID().toString(), 500.0, "Suscripciones", Date(), TransactionType.GASTO, Category.OTROS, PaymentMethod.TARJETA_DEBITO)
        )
        
        _uiState.value = _uiState.value.copy(
            totalIncome = 15000.0,
            categories = initialCategories,
            recentTransactions = initialTransactions
        )
        updateFinancialState()
    }

    private fun updateFinancialState() {
        val categories = _uiState.value.categories
        if (categories.isEmpty()) return

        val underBudgetCount = categories.count { it.spent <= it.budget }
        val percentage = (underBudgetCount.toFloat() / categories.size) * 100

        val newState = when {
            percentage >= 100f -> FinancialState.PERFECT
            percentage >= 75f -> FinancialState.GOOD
            percentage >= 50f -> FinancialState.STABLE
            percentage >= 25f -> FinancialState.WARNING
            else -> FinancialState.CRITICAL
        }

        _uiState.value = _uiState.value.copy(financialState = newState)
        _piggoState.value = newState
    }

    fun addTransaction(amount: Double, description: String, type: TransactionType, category: Category, isGoal: Boolean, date: Date) {
        val tx = Transaction(
            id = UUID.randomUUID().toString(),
            amount = amount,
            description = description,
            date = date,
            type = type,
            category = category,
            paymentMethod = PaymentMethod.EFECTIVO,
            isGoal = isGoal
        )

        val currentTransactions = _uiState.value.recentTransactions.toMutableList()
        currentTransactions.add(0, tx)
        
        val isIncome = type == TransactionType.INGRESO
        val newTotalIncome = if (isIncome) _uiState.value.totalIncome + amount else _uiState.value.totalIncome

        val updatedCategories = _uiState.value.categories.toMutableList()
        if (!isIncome) {
            val index = updatedCategories.indexOfFirst { it.category == category }
            if (index != -1) {
                updatedCategories[index] = updatedCategories[index].copy(spent = updatedCategories[index].spent + amount)
            } else {
                updatedCategories.add(CategoryBudget(category, amount, amount, Color.Gray))
            }
        }

        _uiState.value = _uiState.value.copy(
            recentTransactions = currentTransactions,
            totalIncome = newTotalIncome,
            categories = updatedCategories
        )
        updateFinancialState()
    }

    fun updateTransaction(id: String, amount: Double, description: String, type: TransactionType, category: Category, isGoal: Boolean, date: Date) {
        deleteTransaction(id)
        addTransaction(amount, description, type, category, isGoal, date)
    }

    fun deleteTransaction(id: String) {
        val tx = _uiState.value.recentTransactions.find { it.id == id } ?: return
        
        val isIncome = tx.type == TransactionType.INGRESO
        val newTotalIncome = if (isIncome) _uiState.value.totalIncome - tx.amount else _uiState.value.totalIncome

        val updatedCategories = _uiState.value.categories.toMutableList()
        if (!isIncome) {
            val index = updatedCategories.indexOfFirst { it.category == tx.category }
            if (index != -1) {
                updatedCategories[index] = updatedCategories[index].copy(spent = (updatedCategories[index].spent - tx.amount).coerceAtLeast(0.0))
            }
        }

        _uiState.value = _uiState.value.copy(
            recentTransactions = _uiState.value.recentTransactions.filter { it.id != id },
            totalIncome = newTotalIncome,
            categories = updatedCategories
        )
        updateFinancialState()
    }
}

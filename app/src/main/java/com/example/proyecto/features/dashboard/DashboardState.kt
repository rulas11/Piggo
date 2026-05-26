package com.example.proyecto.features.dashboard

import com.example.proyecto.FinancialState
import com.example.proyecto.data.local.TransactionEntity
import com.example.proyecto.features.dashboard.CategoryBudget

data class DashboardState(
    val financialState: FinancialState = FinancialState.STABLE,
    val totalIncome: Double = 0.0,
    val categories: List<CategoryBudget> = emptyList(),
    val recentTransactions: List<TransactionEntity> = emptyList()
) {
    val totalExpenses: Double get() = categories.sumOf { it.spent }
    val balance: Double get() = totalIncome - totalExpenses
}

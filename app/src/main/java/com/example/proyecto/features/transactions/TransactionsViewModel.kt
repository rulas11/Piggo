package com.example.proyecto.features.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto.Category
import com.example.proyecto.TransactionType
import com.example.proyecto.data.local.TransactionEntity
import com.example.proyecto.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class TransactionsViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    val transactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun updateTransaction(
        id: Int,
        amount: Double,
        desc: String,
        isIncome: TransactionType,
        category: Category,
        date: Date
    ) {
        viewModelScope.launch {
            val updatedTransaction = TransactionEntity(
                id = id,
                amount = amount,
                description = desc,
                isIncome = isIncome,
                category = category,
                dateMillis = date,
            )
            repository.addTransaction(updatedTransaction)
        }
    }
}
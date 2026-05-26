package com.example.proyecto.features.transactions

import java.util.Date

data class Transaction(
    val id: Int,
    val amount: Double,
    val description: String,
    val date: Date,
    val type: TransactionType,
    val category: Category,
    val paymentMethod: PaymentMethod,
    val creditCardPaymentDate: Date? = null,
    val isCompleted: Boolean = true,
    val isGoal: Boolean = false
)
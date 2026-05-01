package com.example.proyecto.model

import java.util.Date

data class Transaction(
    val id: String,
    val amount: Double,
    val description: String,
    val date: Date,
    val type: TransactionType,
    val category: Category,
    val paymentMethod: PaymentMethod,
    val creditCardPaymentDate: Date? = null,
    val isCompleted: Boolean = true,
    val isGoal: Boolean = false // Requisito: preguntar si es meta
)

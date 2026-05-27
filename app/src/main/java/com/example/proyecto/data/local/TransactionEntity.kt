package com.example.proyecto.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.proyecto.Category
import com.example.proyecto.TransactionType
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val isIncome: TransactionType,
    val category: Category,
    val dateMillis: Date,
    val description: String? = null
)
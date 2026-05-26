package com.example.proyecto

import android.app.Application
import com.example.proyecto.data.local.PiggoDatabase
import com.example.proyecto.data.repository.TransactionRepository

class PiggoApplication : Application() {
    val database by lazy { PiggoDatabase.getInstance(this) }
    val repository by lazy { TransactionRepository(database.transactionDao) }
}
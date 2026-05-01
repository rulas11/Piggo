package com.example.proyecto

import com.example.proyecto.features.dashboard.PiggoViewModel
import com.example.proyecto.model.Category
import com.example.proyecto.model.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class ExpenseLogicTest {

    private lateinit var viewModel: PiggoViewModel

    @Before
    fun setup() {
        viewModel = PiggoViewModel()
    }

    @Test
    fun `cuando se agrega un gasto el balance disminuye correctamente`() {
        // Estado inicial
        val initialBalance = viewModel.uiState.value.balance
        val expenseAmount = 500.0

        // Acción: Agregar un gasto
        viewModel.addTransaction(
            amount = expenseAmount,
            description = "Prueba Gasto",
            type = TransactionType.GASTO,
            category = Category.ALIMENTACION,
            isGoal = false,
            date = Date()
        )

        // Verificación
        val finalBalance = viewModel.uiState.value.balance
        assertEquals(initialBalance - expenseAmount, finalBalance, 0.01)
    }

    @Test
    fun `cuando se agrega un gasto la categoria correspondiente actualiza su total`() {
        // Estado inicial de la categoría Alimentación
        val initialSpent = viewModel.uiState.value.categories.find { it.category == Category.ALIMENTACION }?.spent ?: 0.0
        val expenseAmount = 200.0

        // Acción: Agregar gasto a Alimentación
        viewModel.addTransaction(
            amount = expenseAmount,
            description = "Cena",
            type = TransactionType.GASTO,
            category = Category.ALIMENTACION,
            isGoal = false,
            date = Date()
        )

        // Verificación
        val finalSpent = viewModel.uiState.value.categories.find { it.category == Category.ALIMENTACION }?.spent ?: 0.0
        assertEquals(initialSpent + expenseAmount, finalSpent, 0.01)
    }

    @Test
    fun `cuando se agrega un ingreso el balance aumenta correctamente`() {
        // Estado inicial
        val initialBalance = viewModel.uiState.value.balance
        val incomeAmount = 1000.0

        // Acción: Agregar un ingreso
        viewModel.addTransaction(
            amount = incomeAmount,
            description = "Bono",
            type = TransactionType.INGRESO,
            category = Category.INGRESO_VARIABLE,
            isGoal = false,
            date = Date()
        )

        // Verificación
        val finalBalance = viewModel.uiState.value.balance
        assertEquals(initialBalance + incomeAmount, finalBalance, 0.01)
    }
}

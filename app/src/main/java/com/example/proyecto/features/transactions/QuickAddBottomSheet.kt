package com.example.proyecto.features.transactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAddBottomSheet(
    isExpense: Boolean,
    initialTransaction: Transaction? = null,
    onDismiss: () -> Unit,
    onSave: (Double, String, TransactionType, Category, Boolean, Date) -> Unit
) {
    var amount by remember { mutableStateOf(initialTransaction?.amount?.toString() ?: "") }
    var description by remember { mutableStateOf(initialTransaction?.description ?: "") }
    
    // Categorías filtradas por tipo (Gasto o Ingreso)
    val expenseCategories = listOf(
        Category.VIVIENDA, Category.SALUD, Category.EDUCACION,
        Category.ALIMENTACION, Category.TRANSPORTE, Category.SERVICIOS,
        Category.ENTRETENIMIENTO, Category.OTROS
    )
    val incomeCategories = listOf(Category.INGRESO_FIJO, Category.INGRESO_VARIABLE)
    
    val availableCategories = if (isExpense) expenseCategories else incomeCategories
    
    var selectedCategory by remember { 
        mutableStateOf(initialTransaction?.category ?: availableCategories.first()) 
    }
    var isGoal by remember { mutableStateOf(initialTransaction?.isGoal ?: false) }
    
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialTransaction?.date?.time ?: System.currentTimeMillis()
    )
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val selectedDateText = datePickerState.selectedDateMillis?.let { sdf.format(Date(it)) } ?: sdf.format(Date())

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = if (initialTransaction != null) "Editar Movimiento" else (if (isExpense) "Registrar Gasto" else "Registrar Ingreso"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Monto") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                prefix = { Text("$") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (isExpense) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isGoal, onCheckedChange = { isGoal = it })
                    Text("¿Es una meta?", fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedTextField(
                value = selectedDateText,
                onValueChange = { },
                label = { Text("Fecha") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isExpense) "Categoría de Gasto" else "Tipo de Ingreso", 
                fontWeight = FontWeight.Medium, 
                fontSize = 14.sp
            )
            CategorySelector(selectedCategory, availableCategories) { selectedCategory = it }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { 
                    val amt = amount.toDoubleOrNull() ?: 0.0
                    val date = datePickerState.selectedDateMillis?.let { Date(it) } ?: Date()
                    val type = if (isExpense) TransactionType.GASTO else TransactionType.INGRESO
                    onSave(amt, description, type, selectedCategory, isGoal, date)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = amount.isNotEmpty()
            ) {
                Text(if (initialTransaction != null) "Guardar Cambios" else "Guardar")
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = { TextButton(onClick = { showDatePicker = false }) { Text("OK") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(selected: Category, options: List<Category>, onSelect: (Category) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selected.name.replace("_", " "),
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name.replace("_", " ")) },
                    onClick = { onSelect(option); expanded = false }
                )
            }
        }
    }
}

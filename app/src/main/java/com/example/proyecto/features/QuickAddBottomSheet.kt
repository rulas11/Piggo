package com.example.proyecto.features

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
import com.example.proyecto.Category
import com.example.proyecto.TransactionType
import com.example.proyecto.data.local.TransactionEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAddBottomSheet(
    isExpense: Boolean,
    initialTransaction: TransactionEntity? = null,
    onDismiss: () -> Unit,
    onSave: (Double, String, TransactionType, Category, Date) -> Unit
) {
    var amount by remember { mutableStateOf(initialTransaction?.amount?.toString() ?: "") }
    var description by remember { mutableStateOf(initialTransaction?.description ?: "") }

    var amountError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    val expenseCategories = listOf(
        Category.VIVIENDA, Category.SALUD, Category.EDUCACION,
        Category.ALIMENTACION, Category.TRANSPORTE, Category.SERVICIOS,
        Category.ENTRETENIMIENTO, Category.OTROS
    )
    val incomeCategories = listOf(Category.INGRESO_FIJO, Category.INGRESO_VARIABLE)

    val availableCategories = if (isExpense) expenseCategories else incomeCategories

    var selectedCategory by remember {
        mutableStateOf(
            initialTransaction?.category?.let {
                try {
                    Category.valueOf(it.name.uppercase())
                } catch (e: Exception) {
                    availableCategories.first()
                }
            } ?: availableCategories.first()
        )
    }

    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialTransaction?.dateMillis?.time ?: System.currentTimeMillis()
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
                text = if (isExpense) "Registrar Gasto" else "Registrar Ingreso",
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
                    val finalAmount = amount.toDoubleOrNull() ?: 0.0
                    val isAmountValid = finalAmount > 0.0

                    val isDescriptionValid = description.isNotBlank()

                    amountError = !isAmountValid
                    descriptionError = !isDescriptionValid

                    if (isAmountValid && isDescriptionValid) {
                        onSave(
                            finalAmount,
                            description.trim(),
                            if (isExpense) TransactionType.GASTO else TransactionType.INGRESO,
                            selectedCategory,
                            Date(datePickerState.selectedDateMillis ?: System.currentTimeMillis())
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
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

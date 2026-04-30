package com.example.proyecto.features.dashboard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import com.example.proyecto.FinancialState

class PiggoViewModel  : ViewModel() {
    private val _state = MutableStateFlow(FinancialState.WARNING)
    val piggoState = _state.asStateFlow()

    fun updateFinancials(balance: Double, limit: Double) {
        _state.value = when {
            balance > limit * 0.8 -> FinancialState.EXCELLENT
            balance < limit * 0.1 -> FinancialState.WARNING
            balance < 0 -> FinancialState.CRITICAL
            else -> FinancialState.STABLE
        }
    }
}
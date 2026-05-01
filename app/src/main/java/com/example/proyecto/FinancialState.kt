package com.example.proyecto

enum class FinancialState {
    PERFECT,    // 100% de categorías bajo presupuesto
    GOOD,       // >= 75%
    STABLE,     // >= 50%
    WARNING,    // >= 25%
    CRITICAL    // > 0% o sobregastado total
}

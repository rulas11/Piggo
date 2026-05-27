package com.example.proyecto

enum class TransactionType {
    INGRESO,
    GASTO
}


enum class Category {
    // Categorías de Gasto (Inherentes)
    VIVIENDA,       // Gasto Fijo-Fijo (Debo)
    SALUD,          // Gasto Fijo-Fijo (Debo)
    EDUCACION,      // Gasto Fijo-Fijo (Debo)

    ALIMENTACION,   // Gasto Fijo-Variable (Necesito)
    TRANSPORTE,     // Gasto Fijo-Variable (Necesito)
    SERVICIOS,      // Gasto Fijo-Variable (Necesito)

    ENTRETENIMIENTO, // Gasto Variable-Variable (Quiero)
    OTROS,           // Gasto Variable-Variable (Quiero)

    // Categorías de Ingreso
    INGRESO_FIJO,
    INGRESO_VARIABLE,

    // Sistema
    DINERO_LIBRE
}

enum class FinancialState {
    PERFECT,    // 100% de categorías bajo presupuesto
    GOOD,       // >= 75%
    STABLE,     // >= 50%
    WARNING,    // >= 25%
    CRITICAL    // > 0% o sobregastado total
}

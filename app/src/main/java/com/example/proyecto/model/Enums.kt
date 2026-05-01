package com.example.proyecto.model

enum class TransactionType {
    INGRESO,
    GASTO
}

enum class PaymentMethod {
    EFECTIVO,
    TARJETA_DEBITO,
    TARJETA_CREDITO,
    DEUDA
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

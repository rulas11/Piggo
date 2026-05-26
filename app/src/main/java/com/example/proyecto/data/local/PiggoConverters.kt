package com.example.proyecto.data.local

import androidx.room.TypeConverter
import com.example.proyecto.features.transactions.Category
import com.example.proyecto.features.transactions.TransactionType
import java.util.Date

class PiggoConverters {

    @TypeConverter
    fun fromCategory(category: Category): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(name: String): Category {
        return try {
            Category.valueOf(name)
        } catch (e: IllegalArgumentException) {
            Category.OTROS
        }
    }

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String {
        return type.name
    }

    @TypeConverter
    fun toTransactionType(name: String): TransactionType {
        return TransactionType.valueOf(name)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}
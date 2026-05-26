package com.example.proyecto.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
@TypeConverters(PiggoConverters::class)
abstract class PiggoDatabase : RoomDatabase() {

    abstract val transactionDao: TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: PiggoDatabase? = null

        fun getInstance(context: Context): PiggoDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    PiggoDatabase::class.java,
                    "piggo_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
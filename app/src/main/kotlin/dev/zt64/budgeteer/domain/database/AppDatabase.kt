package dev.zt64.budgeteer.domain.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.zt64.budgeteer.domain.dao.CategoryDao
import dev.zt64.budgeteer.domain.dao.TransactionDao
import dev.zt64.budgeteer.domain.entity.CategoryEntity
import dev.zt64.budgeteer.domain.entity.TransactionEntity
import java.io.File

@Database(entities = [TransactionEntity::class, CategoryEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(): AppDatabase {
            return instance ?: synchronized(this) {
                val dbFile = File(System.getProperty("java.io.tmpdir"), "transaction.db")
                Room
                    .databaseBuilder<AppDatabase>(dbFile.absolutePath)
                    .setDriver(BundledSQLiteDriver())
                    .build()
                    .also { instance = it }
            }
        }
    }
}
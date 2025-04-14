package dev.zt64.budgeteer.domain.database

import androidx.room.*
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.zt64.budgeteer.domain.converter.InstantConverters
import dev.zt64.budgeteer.domain.dao.CategoryDao
import dev.zt64.budgeteer.domain.dao.TransactionDao
import dev.zt64.budgeteer.domain.entity.CategoryEntity
import dev.zt64.budgeteer.domain.entity.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Database(entities = [TransactionEntity::class, CategoryEntity::class], version = 1)
@TypeConverters(InstantConverters::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        private val PREPOPULATE_CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(connection: SQLiteConnection) {
                super.onCreate(connection)

                CoroutineScope(Dispatchers.IO).launch {
                    instance?.let { database ->
                        val categoryDao = database.categoryDao()
                        val defaultCategories = listOf(
                            CategoryEntity(name = "Food", icon = "fastfood", color = 0xFFFF5722),
                            CategoryEntity(name = "Transport", icon = "directions_car", color = 0xFF2196F3),
                            CategoryEntity(name = "Entertainment", icon = "movie", color = 0xFF673AB7),
                            CategoryEntity(name = "Shopping", icon = "shopping_cart", color = 0xFF4CAF50),
                            CategoryEntity(name = "Health", icon = "fitness_center", color = 0xFFE91E63),
                            CategoryEntity(name = "Utilities", icon = "electric_bolt", color = 0xFFFF9800),
                            CategoryEntity(name = "Salary", icon = "monetization_on", color = 0xFF8BC34A),
                            CategoryEntity(name = "Investment", icon = "trending_up", color = 0xFF00BCD4)
                        )

                        defaultCategories.forEach { category ->
                            categoryDao.insert(category)
                        }
                    }
                }
            }
        }

        fun getDatabase(): AppDatabase {
            return instance ?: synchronized(this) {
                val dbFile = File(System.getProperty("java.io.tmpdir"), "transaction.db")

                Room
                    .databaseBuilder<AppDatabase>(dbFile.absolutePath)
                    .setDriver(BundledSQLiteDriver())
                    .addCallback(PREPOPULATE_CALLBACK)
                    .build()
                    .also { instance = it }
            }
        }
    }
}
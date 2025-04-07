package dev.zt64.budgeteer.domain.dao

import androidx.room.*
import dev.zt64.budgeteer.domain.entity.CategoryWithTransactions
import dev.zt64.budgeteer.domain.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(category: TransactionEntity)

    @Update
    suspend fun update(category: TransactionEntity)

    @Delete
    suspend fun delete(category: TransactionEntity)

    @Query("SELECT * FROM CategoryEntity")
    fun getCategoriesWithTransactions(): Flow<List<CategoryWithTransactions>>

    @Query("SELECT * FROM TransactionEntity")
    fun getAllTransactions(): Flow<List<TransactionEntity>>
}
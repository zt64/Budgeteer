package dev.zt64.budgeteer.domain.dao

import androidx.room.*
import dev.zt64.budgeteer.domain.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TransactionDao {
    @Insert
    suspend fun insert(category: TransactionEntity)

    @Update
    suspend fun update(category: TransactionEntity)

    @Delete
    suspend fun delete(category: TransactionEntity)

    @Query("DELETE FROM TransactionEntity WHERE transactionId = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM TransactionEntity")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM TransactionEntity WHERE transactionId = :transactionId")
    suspend fun getTransactionById(transactionId: Int): TransactionEntity?
}
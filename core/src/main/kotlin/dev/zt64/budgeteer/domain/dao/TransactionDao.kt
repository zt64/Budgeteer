package dev.zt64.budgeteer.domain.dao

import androidx.room.*
import dev.zt64.budgeteer.domain.entity.TransactionEntity
import dev.zt64.budgeteer.domain.entity.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TransactionDao {
    @Insert
    suspend fun insert(transaction: TransactionEntity): Long

    @Insert
    suspend fun insertAll(transaction: List<TransactionEntity>): List<Long>

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("DELETE FROM TransactionEntity WHERE transactionId = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM TransactionEntity")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Transaction
    @Query("SELECT * FROM TransactionEntity")
    fun getAllTransactionsWithCategory(): Flow<List<TransactionWithCategory>>

    @Transaction
    @Query("SELECT * FROM TransactionEntity WHERE transactionId = :transactionId")
    suspend fun getTransactionById(transactionId: Int): TransactionWithCategory?
}
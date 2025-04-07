package dev.zt64.budgeteer.domain.manager

import dev.zt64.budgeteer.domain.dao.TransactionDao
import dev.zt64.budgeteer.domain.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionManager(private val transactionDao: TransactionDao) {
    suspend fun addTransaction(transaction: TransactionEntity) {
        transactionDao.insert(transaction)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.update(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.delete(transaction)
    }

    fun getTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions()
    }
}
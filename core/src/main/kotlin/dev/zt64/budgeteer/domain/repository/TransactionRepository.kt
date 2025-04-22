package dev.zt64.budgeteer.domain.repository

import dev.zt64.budgeteer.domain.dao.TransactionDao
import dev.zt64.budgeteer.domain.entity.TransactionEntity
import dev.zt64.budgeteer.domain.entity.TransactionWithCategory
import dev.zt64.budgeteer.domain.model.Transaction
import dev.zt64.budgeteer.domain.toDatabaseEntity
import dev.zt64.budgeteer.domain.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepository internal constructor(private val transactionDao: TransactionDao) {
    suspend fun addTransaction(transaction: Transaction) {
        transactionDao.insert(transaction.toDatabaseEntity())
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.update(transaction.toDatabaseEntity())
    }

    suspend fun deleteTransaction(transactionId: Int) {
        transactionDao.delete(transactionId)
    }

    fun getTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions().map {
            it.map(TransactionEntity::toDomainModel)
        }
    }

    fun getTransactionsWithCategory(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactionsWithCategory().map {
            it.map(TransactionWithCategory::toDomainModel)
        }
    }

    suspend fun getTransaction(transactionId: Int): Transaction? {
        return transactionDao.getTransactionById(transactionId)?.toDomainModel()
    }
}
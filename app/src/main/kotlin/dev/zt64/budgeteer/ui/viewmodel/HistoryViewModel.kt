package dev.zt64.budgeteer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zt64.budgeteer.domain.entity.TransactionEntity
import dev.zt64.budgeteer.domain.manager.TransactionManager
import dev.zt64.budgeteer.domain.model.Transaction
import kotlinx.coroutines.launch

class HistoryViewModel(private val transactionManager: TransactionManager) : ViewModel() {
    val transactions = transactionManager.getTransactions()

    fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionManager.addTransaction(
            TransactionEntity(
                title = transaction.title,
                amount = transaction.amount,
                description = transaction.description
            )
        )
    }
}
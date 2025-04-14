package dev.zt64.budgeteer.ui.viewmodel

import androidx.lifecycle.ViewModel
import dev.zt64.budgeteer.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.take

class HomeViewModel(private val transactionsRepository: TransactionRepository) : ViewModel() {
    val mostRecent = transactionsRepository.getTransactions().take(5)
}
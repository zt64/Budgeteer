package dev.zt64.budgeteer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zt64.budgeteer.domain.model.Transaction
import dev.zt64.budgeteer.domain.repository.CategoryRepository
import dev.zt64.budgeteer.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HistoryViewModel(private val transactionsRepository: TransactionRepository, private val categoryRepository: CategoryRepository) :
    ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val allTransactions = transactionsRepository.getTransactions()

    val categories = categoryRepository.getCategories()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val transactions = combine(allTransactions, searchText) { transactions, query ->
        if (query.isBlank()) {
            transactions
        } else {
            transactions.filter { transaction ->
                transaction.title.contains(query, ignoreCase = true) ||
                    (transaction.description?.contains(query, ignoreCase = true) ?: false)
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionsRepository.addTransaction(transaction)
    }

    fun updateSearchQuery(query: String) {
        _searchText.value = query
    }
}
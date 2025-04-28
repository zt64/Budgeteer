package dev.zt64.budgeteer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zt64.budgeteer.domain.model.Transaction
import dev.zt64.budgeteer.domain.repository.CategoryRepository
import dev.zt64.budgeteer.domain.repository.TransactionRepository
import dev.zt64.budgeteer.ui.model.Filter
import dev.zt64.budgeteer.ui.model.SortBy
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class HistoryViewModel(
    private val transactionsRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    initialFilter: Filter
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private var _filter = MutableStateFlow(initialFilter)
    val filter = _filter.asStateFlow()

    private val allTransactions = transactionsRepository.getTransactionsWithCategory()

    val categories = categoryRepository.getCategories()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val transactions = combine(
        allTransactions,
        searchText,
        filter
    ) { transactions, query, currentFilter ->
        var filtered = transactions

        if (query.isNotBlank()) {
            filtered = filtered.filter { transaction ->
                transaction.title.contains(query, ignoreCase = true) ||
                    (transaction.description?.contains(query, ignoreCase = true) ?: false)
            }
        }

        if (currentFilter.categories.isNotEmpty()) {
            filtered = filtered.filter { transaction ->
                transaction.category in currentFilter.categories
            }
        }

        filtered = filtered.filter { transaction ->
            transaction.amount >= currentFilter.minAmount &&
                transaction.amount <= currentFilter.maxAmount
        }

        filtered = when (currentFilter.sortBy) {
            SortBy.DATE -> filtered.sortedByDescending { it.date }
            SortBy.AMOUNT -> filtered.sortedByDescending { it.amount }
            SortBy.TITLE -> filtered.sortedBy { it.title }
            SortBy.CATEGORY -> filtered.sortedBy { it.category?.name }
        }

        if (currentFilter.sortAscending) {
            filtered = filtered.reversed()
        }

        filtered
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionsRepository.addTransaction(transaction)
    }

    fun updateSearchQuery(query: String) {
        _searchText.value = query
    }

    fun updateFilter(filter: Filter) {
        viewModelScope.launch {
            this@HistoryViewModel._filter.emit(filter)
        }
    }
}
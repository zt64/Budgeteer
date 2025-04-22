package dev.zt64.budgeteer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zt64.budgeteer.domain.model.Category
import dev.zt64.budgeteer.domain.model.Transaction
import dev.zt64.budgeteer.domain.repository.CategoryRepository
import dev.zt64.budgeteer.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.*

internal sealed interface HomeUiState {
    data class Loaded(
        val spendingByCategory: List<Float>,
        val transactions: List<Transaction>,
        val categories: List<Category>
    ) : HomeUiState
    object Loading : HomeUiState
    object Error : HomeUiState
}

class HomeViewModel(private val transactionRepository: TransactionRepository, private val categoryRepository: CategoryRepository) :
    ViewModel() {
    val categories = categoryRepository.getCategories()
    val transactions = transactionRepository.getTransactionsWithCategory()
    val mostRecent = transactions.map { it.take(5) }
    val totalIncome = transactions.map { txs ->
        txs.filter { !it.isExpense }.sumOf { it.amount }
    }
    val totalExpenses = transactions.map { txs ->
        txs.filter { it.isExpense }.sumOf { it.amount }
    }

    val spendingByCategory = combine(transactions, categories) { txs, cats ->
        val categorySpending = txs.groupBy { it.id }
            .mapValues { (_, transactions) ->
                transactions.sumOf { it.amount }
            }

        // Map to category objects with their spending
        cats.associateWith { category ->
            categorySpending[category.id] ?: 5.0
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())
}
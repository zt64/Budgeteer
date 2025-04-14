package dev.zt64.budgeteer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zt64.budgeteer.domain.model.Transaction
import dev.zt64.budgeteer.domain.repository.CategoryRepository
import dev.zt64.budgeteer.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal sealed interface TransactionUiState {
    data class Loaded(val transaction: Transaction, val isEditing: Boolean = false) : TransactionUiState
    object Loading : TransactionUiState
    object Error : TransactionUiState
}

internal class TransactionViewModel(
    private val transactionId: Int,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<TransactionUiState>(TransactionUiState.Loading)
    val uiState = _uiState.asStateFlow()

    val categories = categoryRepository.getCategories()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            transactionRepository.getTransaction(transactionId)?.let { transaction ->
                _uiState.value = TransactionUiState.Loaded(transaction)
            } ?: run {
                _uiState.value = TransactionUiState.Error
            }
        }
    }

    fun deleteTransaction() {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transactionId)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.updateTransaction(transaction)
        }
    }

    fun enterEditMode() {
        val currentState = _uiState.value
        if (currentState is TransactionUiState.Loaded) {
            _uiState.value = currentState.copy(isEditing = true)
        }
    }

    fun exitEditMode() {
        val currentState = _uiState.value
        if (currentState is TransactionUiState.Loaded) {
            _uiState.value = currentState.copy(isEditing = false)
        }
    }
}
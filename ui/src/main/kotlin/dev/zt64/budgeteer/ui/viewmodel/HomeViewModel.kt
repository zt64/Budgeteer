package dev.zt64.budgeteer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zt64.budgeteer.domain.model.Transaction
import dev.zt64.budgeteer.domain.repository.CategoryRepository
import dev.zt64.budgeteer.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlin.time.Instant

class HomeViewModel(private val transactionRepository: TransactionRepository, private val categoryRepository: CategoryRepository) :
    ViewModel() {
    private val categories = categoryRepository.getCategories()
    private val transactions = transactionRepository.getTransactionsWithCategory()

    val mostRecent = transactions.map { it.take(5) }
    val totalIncome = transactions.map { txs ->
        txs.filter { !it.isExpense }.sumOf { it.amount }
    }
    val totalExpenses = transactions.map { txs ->
        txs.filter { it.isExpense }.sumOf { it.amount }
    }
    val netFlow = combine(totalIncome, totalExpenses) { income, expenses ->
        income - expenses
    }

    val spendingByCategory = combine(transactions, categories) { txs, cats ->
        txs
            .filter { it.category != null }
            .groupBy { it.category!! }
            .mapValues { (_, transactions) ->
                transactions.sumOf { it.amount }
            }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    suspend fun importTransactions(csv: String): Boolean {
        val lines = csv.trim().lines()
        if (lines.size < 2) {
            println("CSV file is empty or has no data")
            return false
        }

        val header = lines.first().split(",")
        if (header.size != 6 ||
            header[0] != "Title" ||
            header[1] != "Amount" ||
            header[2] != "IsExpense" ||
            header[3] != "Date" ||
            header[4] != "Description" ||
            header[5] != "Category"
        ) {
            println("CSV file has invalid header")
            return false
        }

        val transactionsToInsert = buildList {
            for (line in lines.drop(1)) {
                val parts = line.split(",")
                if (parts.size != 6) {
                    println("Line '$line' has invalid number of columns")
                    return false
                }

                val title = parts[0]
                val amount = parts[1].toDoubleOrNull() ?: return false
                val isExpense = parts[2].toBooleanStrictOrNull() ?: return false
                val date = Instant.parse(parts[3])
                val description = parts[4].ifBlank { null }
                val categoryId = parts[5].toIntOrNull()

                add(
                    Transaction(
                        title = title,
                        amount = amount,
                        isExpense = isExpense,
                        date = date,
                        description = description,
                        category = categoryId?.let { categoryRepository.getCategory(it) }
                    )
                )
            }
        }

        transactionRepository.addTransactions(transactionsToInsert)
        return true
    }

    suspend fun exportToCsv(): String {
        return buildString {
            appendLine("Title,Amount,Date,Description,Category")
            transactions.first().forEach { transaction ->
                appendLine(
                    "${transaction.title}," +
                        "${transaction.amount}," +
                        "${transaction.isExpense}," +
                        "${transaction.date}," +
                        "${transaction.description.orEmpty()}," +
                        "${transaction.category?.id}"
                )
            }
        }
    }

    suspend fun resetData() {
        transactionRepository.deleteAll()
    }
}
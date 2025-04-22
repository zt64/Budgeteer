package dev.zt64.budgeteer.domain.model

import androidx.compose.runtime.Stable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Stable
data class Transaction(
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val isExpense: Boolean = false,
    val date: Instant = Clock.System.now(),
    val description: String? = null,
    val category: Category? = null
)
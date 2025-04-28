package dev.zt64.budgeteer.ui.model

import androidx.compose.runtime.Stable
import dev.zt64.budgeteer.domain.model.Category

@Stable
internal data class Filter(
    val categories: List<Category> = emptyList(),
    val minAmount: Float = 0f,
    val maxAmount: Float = 10000f,
    val sortBy: SortBy = SortBy.DATE,
    val sortAscending: Boolean = false
)
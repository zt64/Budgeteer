package dev.zt64.budgeteer.domain.model

import androidx.compose.runtime.Stable

@Stable
data class Category(
    val id: Int = 0,
    val name: String,
    val icon: String?,
    val color: Long
)
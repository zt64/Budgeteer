package dev.zt64.budgeteer.domain.model

data class Transaction(
    val title: String,
    val amount: Double,
    val description: String? = null
)
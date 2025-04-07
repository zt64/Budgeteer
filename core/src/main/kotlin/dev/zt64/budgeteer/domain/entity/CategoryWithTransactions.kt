package dev.zt64.budgeteer.domain.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithTransactions(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "transactionId"
    )
    val transactions: List<TransactionEntity>
)
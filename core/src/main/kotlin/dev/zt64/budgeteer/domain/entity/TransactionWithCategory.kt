package dev.zt64.budgeteer.domain.entity

import androidx.room.Embedded
import androidx.room.Relation

internal data class TransactionWithCategory(
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val category: CategoryEntity?
)
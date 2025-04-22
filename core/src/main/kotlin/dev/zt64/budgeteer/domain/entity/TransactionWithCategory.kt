package dev.zt64.budgeteer.domain.entity

import androidx.room.Embedded
import androidx.room.Relation

internal data class TransactionWithCategory(
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = "transactionId", // FK in TransactionEntity
        entityColumn = "categoryId" // PK in CategoryEntity
    )
    val category: CategoryEntity?
)
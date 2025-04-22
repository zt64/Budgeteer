@file:OptIn(ExperimentalTime::class)

package dev.zt64.budgeteer.domain

import dev.zt64.budgeteer.domain.entity.CategoryEntity
import dev.zt64.budgeteer.domain.entity.TransactionEntity
import dev.zt64.budgeteer.domain.entity.TransactionWithCategory
import dev.zt64.budgeteer.domain.model.Category
import dev.zt64.budgeteer.domain.model.Transaction
import kotlin.time.ExperimentalTime

internal fun TransactionEntity.toDomainModel(): Transaction {
    return Transaction(
        id = transactionId,
        title = title,
        amount = amount,
        isExpense = isExpense,
        date = date,
        description = description,
        category = null
    )
}

internal fun Transaction.toDatabaseEntity(): TransactionEntity {
    return TransactionEntity(
        transactionId = id,
        title = title,
        amount = amount,
        isExpense = isExpense,
        date = date,
        description = description,
        categoryId = category?.id
    )
}

internal fun CategoryEntity.toDomainModel(): Category {
    return Category(
        name = name,
        icon = icon,
        color = color
    )
}

internal fun Category.toDatabaseEntity(): CategoryEntity {
    return CategoryEntity(
        name = name,
        icon = icon,
        color = color
    )
}

internal fun TransactionWithCategory.toDomainModel(): Transaction {
    return Transaction(
        id = transaction.transactionId,
        title = transaction.title,
        amount = transaction.amount,
        date = transaction.date,
        description = transaction.description,
        category = category?.toDomainModel()
    )
}
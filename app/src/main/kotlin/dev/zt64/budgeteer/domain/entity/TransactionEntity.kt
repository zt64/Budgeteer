package dev.zt64.budgeteer.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val transactionId: Int = 0,
    val amount: Double,
    val title: String,
    val description: String? = null
)
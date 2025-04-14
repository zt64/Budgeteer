package dev.zt64.budgeteer.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Entity
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Int = 0,
    val title: String,
    val amount: Double,
    val date: Instant,
    val description: String? = null,
    val categoryId: Int? = null
)
package dev.zt64.budgeteer.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class CategoryEntity(
    @PrimaryKey
    val name: String,
    val icon: String? = null,
    val color: Long
)
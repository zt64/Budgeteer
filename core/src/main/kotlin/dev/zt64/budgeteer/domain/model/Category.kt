package dev.zt64.budgeteer.domain.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class Category(
    val id: Int = 0,
    val name: String,
    val icon: String? = null,
    val color: Color? = null
)
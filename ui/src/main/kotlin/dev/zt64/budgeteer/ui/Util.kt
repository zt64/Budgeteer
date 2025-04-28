package dev.zt64.budgeteer.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import dev.zt64.budgeteer.domain.model.Category

internal val CATEGORY_ICONS = mapOf(
    "expense" to Icons.Default.Money,
    "food" to Icons.Default.Fastfood,
    "transport" to Icons.Default.DirectionsCar,
    "entertainment" to Icons.Default.Movie,
    "shopping" to Icons.Default.ShoppingCart,
    "health" to Icons.Default.FitnessCenter,
    "travel" to Icons.Default.AirplanemodeActive,
    "utilities" to Icons.Default.ElectricBolt,
    "education" to Icons.Default.School,
    "salary" to Icons.Default.MonetizationOn,
    "gift" to Icons.Default.CardGiftcard,
    "investment" to Icons.AutoMirrored.Filled.TrendingUp,
    "other" to Icons.Default.Info
)

internal val Category.iconAsImageVector: ImageVector
    get() = CATEGORY_ICONS[this.icon] ?: Icons.Default.Info
package dev.zt64.budgeteer.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import dev.zt64.budgeteer.domain.model.Category

val Category.iconAsImageVector: ImageVector
    get() = when (icon) {
        "food" -> Icons.Default.Fastfood
        "transport" -> Icons.Default.DirectionsCar
        "entertainment" -> Icons.Default.Movie
        "shopping" -> Icons.Default.ShoppingCart
        "health" -> Icons.Default.FitnessCenter
        "travel" -> Icons.Default.AirplanemodeActive
        "utilities" -> Icons.Default.ElectricBolt
        "education" -> Icons.Default.School
        "salary" -> Icons.Default.MonetizationOn
        "gift" -> Icons.Default.CardGiftcard
        "investment" -> Icons.AutoMirrored.Filled.TrendingUp
        else -> Icons.Default.Info
    }
package dev.zt64.budgeteer.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(val icon: ImageVector, val label: String) {
    HOME(
        icon = Icons.Default.Home,
        label = "Home"
    ),
    HISTORY(
        icon = Icons.Default.History,
        label = "History"
    )
}
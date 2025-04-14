package dev.zt64.budgeteer.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(val icon: ImageVector, val label: String) {
    data object Home : Destination(Icons.Default.Home, "Home")
    data object History : Destination(Icons.Default.History, "History")
    data class Transaction(val id: Int) : Destination(Icons.Default.History, "Transaction")
}
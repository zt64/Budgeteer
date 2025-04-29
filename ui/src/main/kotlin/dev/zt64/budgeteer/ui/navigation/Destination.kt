package dev.zt64.budgeteer.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import dev.zt64.budgeteer.domain.model.Category
import dev.zt64.budgeteer.ui.model.Filter

internal sealed class Destination(val icon: ImageVector, val label: String) {
    data object Home : Destination(Icons.Default.Home, "Home")

    open class History(val filter: Filter = Filter()) : Destination(Icons.Default.History, "History") {
        constructor(category: Category) : this(Filter(categories = setOf(category)))

        companion object Default : History()

        override fun toString(): String {
            return "History(filter=$filter)"
        }
    }

    data class Transaction(val id: Int) : Destination(Icons.Default.History, "Transaction")
}
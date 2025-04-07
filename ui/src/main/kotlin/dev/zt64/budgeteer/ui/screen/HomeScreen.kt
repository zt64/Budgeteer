package dev.zt64.budgeteer.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Dashboard screen that shows an overview of the user's finances.
 */
@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            Row {
                Card {
                    Box {
                        Text("Average Income")
                    }
                }

                Card {
                }
            }

            // TODO: Add most recent transactions up to 5
            Column {
            }
        }
    }
}
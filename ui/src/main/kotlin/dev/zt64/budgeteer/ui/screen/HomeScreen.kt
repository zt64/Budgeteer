package dev.zt64.budgeteer.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.ui.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Dashboard screen that shows an overview of the user's finances.
 */
@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Average Income")
                    }
                }

                Card {
                }
            }

            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Most Recent Transactions",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(Modifier.height(12.dp))

                    val mostRecent by viewModel.mostRecent.collectAsState(emptyList())

                    if (mostRecent.isEmpty()) {
                        Text("No transactions found.")
                    } else {
                        for (transaction in mostRecent) {
                            Column(
                                modifier = Modifier
                                    .clickable {
                                    }
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row {
                                    Text(
                                        text = transaction.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(Modifier.weight(1f))

                                    Text(
                                        text = "$%.2f".format(transaction.amount),
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }

                                transaction.description?.let {
                                    Text(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
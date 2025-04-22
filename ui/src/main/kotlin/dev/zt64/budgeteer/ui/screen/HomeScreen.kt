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
import dev.zt64.budgeteer.ui.navigation.Destination
import dev.zt64.budgeteer.ui.navigation.LocalNavigationManager
import dev.zt64.budgeteer.ui.navigation.currentOrThrow
import dev.zt64.budgeteer.ui.viewmodel.HomeViewModel
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import org.koin.compose.viewmodel.koinViewModel

/**
 * Dashboard screen that shows an overview of the user's finances.
 */
@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
internal fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val nav = LocalNavigationManager.currentOrThrow

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Total Income")

                    val totalIncome by viewModel.totalIncome.collectAsState(0.0)

                    Text(
                        text = "$%.2f".format(totalIncome),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Total Expenses")

                    val totalExpenses by viewModel.totalExpenses.collectAsState(0.0)

                    Text(
                        text = "$%.2f".format(totalExpenses),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Spending by Category")

                    val spendingByCategory by viewModel.spendingByCategory.collectAsState()

                    PieChart(
                        values = spendingByCategory.values.map { it.toFloat() },
                        label = { i ->
                            Text("$%.2f".format(spendingByCategory.values.elementAt(i)))
                        }
                    )
                }
            }
        }

        Card {
            Column(
                modifier = Modifier
                    .width(300.dp)
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
                                    nav.navigate(Destination.Transaction(transaction.id))
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
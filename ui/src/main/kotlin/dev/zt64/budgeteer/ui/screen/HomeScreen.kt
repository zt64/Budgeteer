package dev.zt64.budgeteer.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.ui.LocalSnackbarHostState
import dev.zt64.budgeteer.ui.component.HeaderText
import dev.zt64.budgeteer.ui.component.card.InfoCard
import dev.zt64.budgeteer.ui.navigation.Destination
import dev.zt64.budgeteer.ui.navigation.LocalNavigationManager
import dev.zt64.budgeteer.ui.navigation.currentOrThrow
import dev.zt64.budgeteer.ui.theme.ExpenseContainerColor
import dev.zt64.budgeteer.ui.theme.IncomeContainerColor
import dev.zt64.budgeteer.ui.theme.NetFlowContainerColor
import dev.zt64.budgeteer.ui.viewmodel.HomeViewModel
import dev.zt64.budgeteer.util.Formatter
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.generateHueColorPalette
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.writeString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val nav = LocalNavigationManager.currentOrThrow
        val snackbarHostState = LocalSnackbarHostState.current

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoCard(
                modifier = Modifier.weight(1f),
                title = "Total Income",
                colors = CardDefaults.cardColors(
                    containerColor = IncomeContainerColor
                )
            ) {
                val totalIncome by viewModel.totalIncome.collectAsState(0.0)

                Text(
                    text = Formatter.formatMoney(totalIncome),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            InfoCard(
                modifier = Modifier.weight(1f),
                title = "Total Expenses",
                colors = CardDefaults.cardColors(
                    containerColor = ExpenseContainerColor
                ),
                textStyle = MaterialTheme.typography.titleLarge
            ) {
                val totalExpenses by viewModel.totalExpenses.collectAsState(0.0)

                Text(
                    text = Formatter.formatMoney(totalExpenses),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            InfoCard(
                modifier = Modifier.weight(1f),
                title = "Net Flow",
                colors = CardDefaults.cardColors(
                    containerColor = NetFlowContainerColor
                )
            ) {
                val netFlow by viewModel.netFlow.collectAsState(0.0)

                Text(
                    text = Formatter.formatMoney(netFlow),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        InfoCard(
            modifier = Modifier.fillMaxWidth(),
            title = "Spending by Category"
        ) {
            val spendingByCategory by viewModel.spendingByCategory.collectAsState()

            if (spendingByCategory.isEmpty()) {
                Text("No data available.")
            } else {
                val values = spendingByCategory.values.map { it.toFloat() }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    PieChart(
                        values = values,
                        label = { i ->
                            val (category, amount) = spendingByCategory.entries.elementAt(i)

                            Text(
                                text = "${Formatter.formatMoney(amount)} - ${category.name}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        slice = { index ->
                            val colors = remember(values.size) { generateHueColorPalette(values.size) }
                            val category = spendingByCategory.keys.elementAt(index)

                            DefaultSlice(
                                color = category.color ?: colors[index],
                                clickable = true,
                                antiAlias = true,
                                onClick = { nav.navigate(Destination.History(category)) }
                            )
                        }
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            val mostRecent by viewModel.mostRecent.collectAsState(emptyList())

            HeaderText(
                text = "Most Recent Transactions",
                modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleLarge
            )

            if (mostRecent.isEmpty()) {
                Text(
                    text = "No data available.",
                    modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)
                )
            } else {
                mostRecent.forEachIndexed { index, transaction ->
                    Row(
                        modifier = Modifier
                            .clickable {
                                nav.navigate(Destination.Transaction(transaction.id))
                            }
                            .background(
                                if (index % 2 == 0) {
                                    MaterialTheme.colorScheme.surfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.surfaceBright
                                }
                            )
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = transaction.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            transaction.description?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(Modifier.weight(1f))

                        Text(
                            text = Formatter.formatMoney(transaction.amount),
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = if (transaction.isExpense) {
                                    MaterialTheme.colorScheme.secondary
                                } else {
                                    MaterialTheme.colorScheme.primary
                                }
                            )
                        )
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val scope = rememberCoroutineScope { Dispatchers.IO }
            val importLauncher = rememberFilePickerLauncher(
                mode = FileKitMode.Single,
                type = FileKitType.File("csv")
            ) {
                if (it != null) {
                    scope.launch {
                        if (viewModel.importTransactions(it.readString())) {
                            snackbarHostState.showSnackbar("Imported ${it.name}")
                        } else {
                            snackbarHostState.showSnackbar("Failed to import ${it.name}")
                        }
                    }
                }
            }

            val exportLauncher = rememberFileSaverLauncher { file ->
                if (file != null) {
                    scope.launch {
                        file.writeString(viewModel.exportToCsv())
                        snackbarHostState.showSnackbar("Exported to ${file.name}")
                    }
                }
            }

            OutlinedButton(
                onClick = importLauncher::launch,
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    imageVector = Icons.Default.Upload,
                    contentDescription = "Import CSV"
                )
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("Import CSV")
            }

            OutlinedButton(
                onClick = {
                    exportLauncher.launch("transactions", "csv")
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Export CSV"
                )
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("Export CSV")
            }

            Spacer(Modifier.weight(1f))

            OutlinedButton(
                onClick = {
                    scope.launch {
                        viewModel.resetData()
                        snackbarHostState.showSnackbar("Data reset")
                    }
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Reset Data"
                )
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("Reset Data")
            }
        }
    }
}
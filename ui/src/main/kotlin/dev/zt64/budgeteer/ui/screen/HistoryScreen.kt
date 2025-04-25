package dev.zt64.budgeteer.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.domain.model.Transaction
import dev.zt64.budgeteer.ui.LocalSnackbarHostState
import dev.zt64.budgeteer.ui.dialog.AddTransactionDialog
import dev.zt64.budgeteer.ui.dialog.FilterDialog
import dev.zt64.budgeteer.ui.iconAsImageVector
import dev.zt64.budgeteer.ui.navigation.Destination
import dev.zt64.budgeteer.ui.navigation.LocalNavigationManager
import dev.zt64.budgeteer.ui.navigation.currentOrThrow
import dev.zt64.budgeteer.ui.viewmodel.HistoryViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

/**
 * An overview of all transactions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScreen() {
    val viewModel = koinViewModel<HistoryViewModel>()
    val snackbarHostState = LocalSnackbarHostState.current

    var showFilterDialog by remember { mutableStateOf(false) }

    if (showFilterDialog) {
        val currentFilter by viewModel.filter.collectAsState()
        FilterDialog(
            filter = currentFilter,
            availableCategories = viewModel.categories.collectAsState(emptyList()).value,
            onDismissRequest = { showFilterDialog = false },
            onConfirm = { filter ->
                viewModel.updateFilter(filter)
                showFilterDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val searchText by viewModel.searchText.collectAsState()

                TextField(
                    value = searchText,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    label = { Text("Search") },
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { showFilterDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->
        val transactions by viewModel.transactions.collectAsState(emptyList())
        val scope = rememberCoroutineScope()

        var showDialog by remember { mutableStateOf(false) }

        if (showDialog) {
            val categories by viewModel.categories.collectAsState(emptyList())
            AddTransactionDialog(
                categories = categories,
                onConfirm = {
                    viewModel.addTransaction(it)
                    scope.launch {
                        snackbarHostState.showSnackbar("Transaction added")
                    }
                },
                onDismissRequest = {
                    showDialog = false
                }
            )
        }

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (transactions.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )

                    Text(
                        text = "No transactions added",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Button(
                        onClick = { showDialog = true }
                    ) {
                        Text("Add Transaction")
                    }
                }
            } else {
                val navigationManager = LocalNavigationManager.currentOrThrow

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(transactions) { transaction ->
                        TransactionCard(
                            transaction = transaction,
                            onClick = {
                                navigationManager.navigate(Destination.Transaction(transaction.id))
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(64.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = { showDialog = true },
                                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                                Text("Add Transaction")
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun TransactionCard(
    transaction: Transaction,
    onClick: () -> Unit
) {
    OutlinedCard {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            transaction.category?.iconAsImageVector?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = transaction.title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Format and display the date
                    Text(
                        text = DateTimeFormatter
                            .ofPattern("MM-dd-yyyy h:mm a")
                            .withZone(ZoneId.systemDefault()) // Specify time zone for the Instant
                            .format(transaction.date.toJavaInstant()),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Display category name
                transaction.category?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                transaction.description?.takeUnless { it.isEmpty() }?.let {
                    Text(it)
                }
            }

            Spacer(Modifier.weight(0.2f))

            Text(
                text = "$%.2f".format(transaction.amount),
                style = MaterialTheme.typography.titleMedium,
                color = if (transaction.amount >= 0) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }
    }
}
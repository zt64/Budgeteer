package dev.zt64.budgeteer.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.domain.model.Transaction
import dev.zt64.budgeteer.ui.LocalSnackbarHostState
import dev.zt64.budgeteer.ui.dialog.AddTransactionDialog
import dev.zt64.budgeteer.ui.iconAsImageVector
import dev.zt64.budgeteer.ui.navigation.Destination
import dev.zt64.budgeteer.ui.navigation.LocalNavigationManager
import dev.zt64.budgeteer.ui.navigation.currentOrThrow
import dev.zt64.budgeteer.ui.viewmodel.HistoryViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/**
 * An overview of all transactions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen() {
    val viewModel = koinViewModel<HistoryViewModel>()
    val snackbarHostState = LocalSnackbarHostState.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = { /* Handle search action */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search transactions"
                        )
                    }
                }
            )
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

                LazyColumn {
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

@Composable
private fun TransactionCard(
    transaction: Transaction,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        leadingContent = {
            transaction.category?.iconAsImageVector?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }
        },
        headlineContent = {
            Text(transaction.title)
        },
        supportingContent = transaction.description?.takeUnless { it.isEmpty() }?.let { { Text(it) } },
        trailingContent = {
            Text("$%.2f".format(transaction.amount))
        }
    )
}
package dev.zt64.budgeteer.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.domain.model.Transaction
import dev.zt64.budgeteer.ui.LocalSnackbarHostState
import dev.zt64.budgeteer.ui.iconAsImageVector
import dev.zt64.budgeteer.ui.model.Filter
import dev.zt64.budgeteer.ui.model.SortBy
import dev.zt64.budgeteer.ui.navigation.Destination
import dev.zt64.budgeteer.ui.navigation.LocalNavigationManager
import dev.zt64.budgeteer.ui.navigation.currentOrThrow
import dev.zt64.budgeteer.ui.viewmodel.HistoryViewModel
import dev.zt64.budgeteer.ui.widget.dialog.AddTransactionDialog
import dev.zt64.budgeteer.ui.widget.dialog.FilterDialog
import dev.zt64.budgeteer.util.Formatter
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.ExperimentalTime

/**
 * An overview of all transactions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScreen(initialFilter: Filter = Filter()) {
    val viewModel = koinViewModel<HistoryViewModel> { parametersOf(initialFilter) }
    val categories by viewModel.categories.collectAsState(emptyList())

    var showFilterDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(initialFilter) {
        viewModel.updateFilter(initialFilter)
    }

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

    if (showAddDialog) {
        val scope = rememberCoroutineScope()

        AddTransactionDialog(
            categories = categories,
            onConfirm = {
                scope.launch {
                    viewModel.addTransaction(it)
                    snackbarHostState.showSnackbar("Transaction added")
                }
            },
            onDismissRequest = { showAddDialog = false }
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
                    modifier = Modifier.weight(1f),
                    value = searchText,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    label = { Text("Search") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    trailingIcon = if (searchText.isNotEmpty()) {
                        {
                            IconButton(
                                onClick = { viewModel.updateSearchQuery("") }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search"
                                )
                            }
                        }
                    } else {
                        null
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    shape = CircleShape
                )

                FilledTonalIconButton(
                    onClick = { showFilterDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Transaction"
                )
            }
        }
    ) { paddingValues ->
        val transactions by viewModel.transactions.collectAsState(emptyList())
        val filter by viewModel.filter.collectAsState()

        val navigationManager = LocalNavigationManager.currentOrThrow

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    if (filter.sortBy != SortBy.DATE) {
                        InputChip(
                            selected = true,
                            onClick = { },
                            label = {
                                Text(filter.sortBy.displayName)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Sort,
                                    contentDescription = null
                                )
                            }
                        )
                    }

                    filter.categories.forEach {
                        FilterChip(
                            selected = true,
                            onClick = {
                                viewModel.updateFilter(filter.copy(categories = filter.categories - it))
                            },
                            label = { Text(it.name) },
                            leadingIcon = {
                                Icon(
                                    imageVector = it.iconAsImageVector,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    val showClearButton by derivedStateOf { filter != Filter() }

                    if (showClearButton) {
                        AssistChip(
                            onClick = { viewModel.updateFilter(Filter()) },
                            label = { Text("Reset filter") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }

            if (transactions.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )

                        Text(
                            text = "No transactions found",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            } else {
                items(transactions) { transaction ->
                    TransactionCard(
                        transaction = transaction,
                        onClick = {
                            navigationManager.navigate(Destination.Transaction(transaction.id))
                        }
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { showAddDialog = true },
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

@OptIn(ExperimentalTime::class)
@Composable
private fun TransactionCard(transaction: Transaction, onClick: () -> Unit) {
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
                }

                transaction.category?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                transaction.description?.takeUnless { it.isEmpty() }?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = Formatter.formatMoney(transaction.amount),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = Formatter.formatDate(transaction.date),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
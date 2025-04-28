package dev.zt64.budgeteer.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.ui.LocalSnackbarHostState
import dev.zt64.budgeteer.ui.component.field.MoneyInputField
import dev.zt64.budgeteer.ui.iconAsImageVector
import dev.zt64.budgeteer.ui.navigation.LocalNavigationManager
import dev.zt64.budgeteer.ui.navigation.currentOrThrow
import dev.zt64.budgeteer.ui.viewmodel.TransactionUiState
import dev.zt64.budgeteer.ui.viewmodel.TransactionViewModel
import dev.zt64.budgeteer.ui.widget.dialog.ConfirmDeleteDialog
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun TransactionScreen(transactionId: Int) {
    val viewModel = koinViewModel<TransactionViewModel> { parametersOf(transactionId) }
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is TransactionUiState.Loaded -> {
            LoadedTransactionScreen(transactionId)
        }
        TransactionUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        TransactionUiState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    modifier = Modifier.size(64.dp)
                )
                Text("Error loading transaction")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoadedTransactionScreen(transactionId: Int) {
    val viewModel = koinViewModel<TransactionViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val navigationManager = LocalNavigationManager.currentOrThrow
    val snackbarHostState = LocalSnackbarHostState.current
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    if (showDeleteDialog) {
        ConfirmDeleteDialog(
            title = "Delete Transaction",
            message = "Are you sure you want to delete this transaction?",
            onConfirm = {
                scope.launch {
                    viewModel.deleteTransaction()
                    snackbarHostState.showSnackbar("Transaction deleted")
                }
                navigationManager.navigateUp()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    when (uiState) {
        is TransactionUiState.Loaded -> {
            val state = uiState as TransactionUiState.Loaded
            val transaction = state.transaction
            val categories by viewModel.categories.collectAsState(emptyList())

            var title by rememberSaveable { mutableStateOf(transaction.title) }
            var amount by rememberSaveable { mutableStateOf(transaction.amount) }
            var isExpense by rememberSaveable { mutableStateOf(transaction.isExpense) }
            var category by rememberSaveable { mutableStateOf(transaction.category) }
            var date by rememberSaveable { mutableStateOf(transaction.date) }
            var description by rememberSaveable { mutableStateOf(transaction.description.orEmpty()) }

            LaunchedEffect(state.isEditing) {
                if (!state.isEditing) {
                    title = transaction.title
                    amount = transaction.amount
                    category = transaction.category
                    description = transaction.description.orEmpty()
                }
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                if (state.isEditing) "Edit Transaction" else "Transaction Details"
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.exitEditMode()
                                    navigationManager.navigateUp()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Navigate back"
                                )
                            }
                        },
                        actions = {
                            if (state.isEditing) {
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            val updatedTransaction = transaction.copy(
                                                title = title,
                                                amount = amount,
                                                isExpense = isExpense,
                                                category = category,
                                                description = description
                                            )
                                            viewModel.updateTransaction(updatedTransaction)
                                            viewModel.exitEditMode()
                                            snackbarHostState.showSnackbar("Transaction updated")
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save changes"
                                    )
                                }
                                IconButton(onClick = viewModel::exitEditMode) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cancel editing"
                                    )
                                }
                            } else {
                                IconButton(onClick = viewModel::enterEditMode) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit transaction"
                                    )
                                }
                            }
                        }
                    )
                },
                bottomBar = {
                    Surface {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
                        ) {
                            Button(
                                onClick = { showDeleteDialog = true },
                                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete transaction"
                                )
                                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                                Text("Delete")
                            }
                        }
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Column {
                        SingleChoiceSegmentedButtonRow {
                            SegmentedButton(
                                selected = isExpense,
                                onClick = { if (state.isEditing) isExpense = true },
                                enabled = state.isEditing,
                                shape = SegmentedButtonDefaults.itemShape(0, 2)
                            ) {
                                Text("Expense")
                            }
                            SegmentedButton(
                                selected = !isExpense,
                                onClick = { if (state.isEditing) isExpense = false },
                                shape = SegmentedButtonDefaults.itemShape(1, 2)
                            ) {
                                Text("Income")
                            }
                        }
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Name") },
                            readOnly = !state.isEditing
                        )
                        MoneyInputField(
                            value = amount,
                            onValueChange = { amount = it },
                            readOnly = !state.isEditing
                        )
                        var expanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
                                value = category?.name.orEmpty(),
                                onValueChange = { },
                                label = { Text("Category") },
                                readOnly = true,
                                leadingIcon = {
                                    Icon(
                                        imageVector = category?.iconAsImageVector ?: Icons.Default.Category,
                                        contentDescription = "Category"
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                }
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                categories.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it.name) },
                                        onClick = {
                                            if (state.isEditing) {
                                                category = it
                                                expanded = false
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            readOnly = !state.isEditing,
                            minLines = 4,
                            maxLines = 4
                        )
                    }
                }
            }
        }
        TransactionUiState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    modifier = Modifier.size(64.dp)
                )
                Text("Error loading transaction")
            }
        }
        TransactionUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
package dev.zt64.budgeteer.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.domain.model.Category
import dev.zt64.budgeteer.domain.model.Transaction
import dev.zt64.budgeteer.ui.iconAsImageVector
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AddTransactionDialog(
    categories: List<Category>,
    onConfirm: (Transaction) -> Unit,
    onDismissRequest: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf(0.0) }
    var category by remember { mutableStateOf<Category?>(null) }
    var description by rememberSaveable { mutableStateOf("") }

    val isValid = remember(title, amount, category) {
        title.isNotBlank() && amount > 0
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                enabled = isValid,
                onClick = {
                    onConfirm(
                        Transaction(
                            title = title,
                            amount = amount,
                            description = description.ifBlank { null },
                            category = category
                        )
                    )
                    onDismissRequest()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        title = {
            Text("Add Transaction")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column {
                        OutlinedTextField(
                            modifier = Modifier.widthIn(min = 40.dp),
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Title") },
                            maxLines = 1
                        )
                    }

                    var expanded by rememberSaveable { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryEditable)
                                .fillMaxWidth(),
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
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.name) },
                                    onClick = {
                                        category = it
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = amount.toString(),
                    onValueChange = { amount = it.toDoubleOrNull() ?: 0.0 },
                    label = { Text("Amount") }
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    maxLines = 5
                )
            }
        }
    )
}
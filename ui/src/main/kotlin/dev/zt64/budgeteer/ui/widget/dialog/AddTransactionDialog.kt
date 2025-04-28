package dev.zt64.budgeteer.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.domain.model.Category
import dev.zt64.budgeteer.domain.model.Transaction
import dev.zt64.budgeteer.ui.component.DateTimeField
import dev.zt64.budgeteer.ui.component.field.MoneyInputField
import dev.zt64.budgeteer.ui.iconAsImageVector
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
internal fun AddTransactionDialog(categories: List<Category>, onConfirm: (Transaction) -> Unit, onDismissRequest: () -> Unit) {
    var title by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf(0.0) }
    var isExpense by rememberSaveable { mutableStateOf(false) }
    var dateTime by rememberSaveable { mutableStateOf(Clock.System.now()) }
    var category by remember { mutableStateOf<Category?>(null) }
    var description by rememberSaveable { mutableStateOf("") }

    val isValid = remember(title, amount, category) {
        title.isNotBlank() && amount > 0
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                enabled = isValid,
                onClick = {
                    onConfirm(
                        Transaction(
                            title = title,
                            amount = amount,
                            isExpense = isExpense,
                            date = dateTime,
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
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = title,
                onValueChange = { text ->
                    title = text
                        .filter { it.isLetterOrDigit() || it == ' ' }
                        .take(32)
                        .trim()
                },
                label = { Text("Title") },
                maxLines = 1
            )

            MoneyInputField(
                value = amount,
                onValueChange = { amount = it }
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
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach {
                    DropdownMenuItem(
                        text = { Text(it.name) },
                        leadingIcon = {
                            Icon(
                                imageVector = it.iconAsImageVector,
                                contentDescription = "Category"
                            )
                        },
                        onClick = {
                            category = it
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange = { description = it.take(200) },
            label = { Text("Description") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Description"
                )
            },
            supportingText = {
                Text("${description.length}/200")
            },
            maxLines = 5
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SingleChoiceSegmentedButtonRow {
                SegmentedButton(
                    selected = isExpense,
                    onClick = { isExpense = true },
                    shape = SegmentedButtonDefaults.itemShape(0, 2)
                ) {
                    Text("Expense")
                }

                SegmentedButton(
                    selected = !isExpense,
                    onClick = { isExpense = false },
                    shape = SegmentedButtonDefaults.itemShape(1, 2)
                ) {
                    Text("Income")
                }
            }

            DateTimeField(
                instant = dateTime,
                onValueChange = { dateTime = it }
            )
        }
    }
}
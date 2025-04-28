package dev.zt64.budgeteer.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.domain.model.Category
import dev.zt64.budgeteer.domain.model.Transaction
import dev.zt64.budgeteer.ui.component.field.MoneyInputField
import dev.zt64.budgeteer.ui.iconAsImageVector
import dev.zt64.budgeteer.util.Formatter
import java.time.ZoneId
import kotlin.time.*
import kotlin.time.Duration.Companion.minutes

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

@Composable
private fun DateTimeField(instant: Instant, onValueChange: (Instant) -> Unit) {
    var showDateDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = instant.toEpochMilliseconds())
    val timePickerState = rememberTimePickerState(
        initialHour = instant.toJavaInstant().atZone(ZoneId.systemDefault()).hour,
        initialMinute = instant.toJavaInstant().atZone(ZoneId.systemDefault()).minute,
        is24Hour = false
    )

    if (showDateDialog) {
        DatePickerDialog(
            onDismissRequest = { showDateDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        // onValueChange()
                        showDateDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDateDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimeDialog) {
        TimePickerDialog(
            state = timePickerState,
            onTimeChange = { minute ->
                onValueChange(Clock.System.now() + minute.minutes)
            },
            onDismissRequest = { showTimeDialog = false }
        )
    }

    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ProvideTextStyle(
            value = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        ) {
            val color = MaterialTheme.colorScheme.tertiary

            Surface(
                onClick = { showDateDialog = true },
                color = color,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = Formatter.formatDate(datePickerState.selectedDateMillis!!)
                )
            }

            Surface(
                onClick = { showTimeDialog = true },
                color = color,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = Formatter.formatTime(
                        timePickerState.hour,
                        timePickerState.minute
                    )
                )
            }
        }
    }
}
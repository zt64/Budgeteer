package dev.zt64.budgeteer.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.domain.model.Category
import dev.zt64.budgeteer.ui.CATEGORY_ICONS

@Composable
internal fun AddCategoryDialog(onDismissRequest: () -> Unit, onConfirm: (Category) -> Unit) {
    var name by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf(Icons.Default.Money) }
    var color by remember { mutableStateOf(Color.Red) }

    Dialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Add Category") },
        confirmButton = {
            Button(
                enabled = name.isNotEmpty(),
                onClick = {
                    onConfirm(
                        Category(
                            name = name,
                            color = color,
                            icon = null
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
        }
    ) {
        TextField(
            value = name,
            onValueChange = { name = it.filter { char -> char.isLetterOrDigit() } },
            label = { Text("Name") },
            maxLines = 1
        )

        Text(
            text = "Icon",
            style = MaterialTheme.typography.labelLarge
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            CATEGORY_ICONS.forEach { (category, icon2) ->
                IconButton(
                    onClick = { icon = icon2 }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = category,
                        tint = if (icon == icon2) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }
    }
}
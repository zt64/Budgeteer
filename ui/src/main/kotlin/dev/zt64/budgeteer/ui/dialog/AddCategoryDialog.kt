package dev.zt64.budgeteer.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun AddCategoryDialog(onDismissRequest: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.Red) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Add Category") },
        confirmButton = {
            Button(
                enabled = name.isNotEmpty(),
                onClick = {
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
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it.filter { char -> char.isLetterOrDigit() } },
                    label = { Text("Name") },
                    maxLines = 1
                )

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
            }
        }
    )
}
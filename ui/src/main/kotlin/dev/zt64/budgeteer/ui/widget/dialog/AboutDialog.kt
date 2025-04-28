package dev.zt64.budgeteer.ui.widget.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AboutDialog(onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info"
            )
        },
        title = { Text("About") },
        confirmButton = {
            OutlinedButton(onClick = onDismissRequest) {
                Text("Close")
            }
        }
    ) {
        Text(
            text = """
                Budgeteer is a personal finance application that helps you track your expenses and income.
                
                Developed for CSCI A360 Software Engineering
            """.trimIndent()
        )
    }
}
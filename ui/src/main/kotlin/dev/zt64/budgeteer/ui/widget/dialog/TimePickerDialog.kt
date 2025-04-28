package dev.zt64.budgeteer.ui.widget.dialog

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
internal fun TimePickerDialog(state: TimePickerState, onConfirm: () -> Unit, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Time") },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    ) {
        TimePicker(
            state = state,
            layoutType = TimePickerLayoutType.Horizontal
        )
    }
}
package dev.zt64.budgeteer.ui.dialog

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
internal fun TimePickerDialog(state: TimePickerState, onTimeChange: (minute: Int) -> Unit, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Time") },
        confirmButton = {
            Button(
                onClick = {
                    onTimeChange(state.hour * 60 + state.minute)
                    onDismissRequest()
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
package dev.zt64.budgeteer.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.ui.widget.dialog.TimePickerDialog
import dev.zt64.budgeteer.util.Formatter
import java.time.*
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@Composable
internal fun DateTimeField(instant: Instant, onValueChange: (Instant) -> Unit) {
    var showDateDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }

    val zone = remember { ZoneId.systemDefault() }
    val initialZoneDateTime = remember { instant.toJavaInstant().atZone(zone) }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = instant.toEpochMilliseconds())
    val timePickerState = rememberTimePickerState(
        initialHour = initialZoneDateTime.hour,
        initialMinute = initialZoneDateTime.minute,
        is24Hour = false
    )

    if (showDateDialog) {
        DatePickerDialog(
            onDismissRequest = { showDateDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        val pickedDate = LocalDate.ofEpochDay(
                            datePickerState.selectedDateMillis!!.div(86_400_000) // 1 day = 86_400_000 ms
                        )
                        val currentTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                        val newInstant = ZonedDateTime.of(pickedDate, currentTime, zone).toInstant()
                        onValueChange(Instant.fromEpochMilliseconds(newInstant.toEpochMilli()))
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
            onConfirm = {
                val pickedDate = Instant
                    .fromEpochMilliseconds(datePickerState.selectedDateMillis!!)
                    .toJavaInstant()
                    .atZone(zone)
                    .toLocalDate()
                val pickedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                val newInstant = ZonedDateTime.of(pickedDate, pickedTime, zone).toInstant()
                onValueChange(Instant.fromEpochMilliseconds(newInstant.toEpochMilli()))
                showTimeDialog = false
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
                    text = Formatter.formatTime(timePickerState.hour, timePickerState.minute)
                )
            }
        }
    }
}
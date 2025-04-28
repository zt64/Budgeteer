package dev.zt64.budgeteer.ui.component.field

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.util.Formatter

@Composable
fun MoneyInputField(
    value: Double,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onValueChange: (Double) -> Unit
) {
    var isAmountError by remember(value) { mutableStateOf(false) }
    var amountValue by remember(value) { mutableStateOf(Formatter.formatMoney(value)) }

    OutlinedTextField(
        modifier = modifier.widthIn(min = 40.dp),
        value = amountValue,
        onValueChange = { text ->
            val cleaned = text
                .filter { it.isDigit() || it == '.' || it == '$' }
                .trim()

            amountValue = cleaned

            val parsed = cleaned.removePrefix("$").toDoubleOrNull()
            val isValid = parsed != null && parsed > 0

            isAmountError = !isValid

            if (isValid) onValueChange(parsed)
        },
        readOnly = readOnly,
        label = { Text("Amount") },
        supportingText = if (isAmountError) {
            { Text("Amount must be a number greater than zero") }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = isAmountError
    )
}
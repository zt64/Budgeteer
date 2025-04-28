package dev.zt64.budgeteer.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun HeaderText(text: String, modifier: Modifier = Modifier, style: TextStyle = MaterialTheme.typography.titleLarge) {
    Text(
        text = text,
        style = style,
        modifier = modifier
    )
}
package dev.zt64.budgeteer.ui.component.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.ui.component.HeaderText

@Composable
internal fun InfoCard(
    title: String,
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(),
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    onClick: (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)? = null
) {
    Card(
        modifier = modifier.then(onClick?.let { Modifier.clickable(onClick = it) } ?: Modifier),
        colors = colors
    ) {
        InfoCardContent(
            title = title,
            textStyle = textStyle,
            leadingIcon = leadingIcon,
            content = content
        )
    }
}

@Composable
internal fun OutlinedInfoCard(
    title: String,
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.outlinedCardColors(),
    textStyle: TextStyle = LocalTextStyle.current,
    onClick: (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)? = null
) {
    OutlinedCard(
        modifier = modifier.then(onClick?.let { Modifier.clickable(onClick = it) } ?: Modifier),
        colors = colors
    ) {
        InfoCardContent(
            title = title,
            textStyle = textStyle,
            leadingIcon = leadingIcon,
            content = content
        )
    }
}

@Composable
private fun InfoCardContent(
    title: String,
    textStyle: TextStyle = LocalTextStyle.current,
    leadingIcon: @Composable (() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)? = null
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.invoke()

            HeaderText(
                text = title,
                style = textStyle
            )
        }

        if (content != null) {
            Spacer(Modifier.height(8.dp))

            content()
        }
    }
}
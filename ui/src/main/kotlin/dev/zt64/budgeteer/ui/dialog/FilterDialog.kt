package dev.zt64.budgeteer.ui.dialog

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.domain.model.Category
import dev.zt64.budgeteer.ui.model.Filter

@Composable
internal fun FilterDialog(
    filter: Filter,
    availableCategories: List<Category>,
    onDismissRequest: () -> Unit,
    onConfirm: (Filter) -> Unit
) {
    var categories by remember { mutableStateOf(filter.categories) }
    var priceRange by remember { mutableStateOf(filter.minAmount..filter.maxAmount) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            OutlinedButton(
                onClick = {
                    onConfirm(
                        filter.copy(
                            categories = categories,
                            minAmount = priceRange.start,
                            maxAmount = priceRange.endInclusive
                        )
                    )
                    onDismissRequest()
                }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        title = {
            Text("Filter Transactions")
        },
        text = {
            Column {
                Text(
                    text = "Value Range",
                    style = MaterialTheme.typography.bodyMedium
                )

                val startInteractionSource = remember { MutableInteractionSource() }
                val endInteractionSource = remember { MutableInteractionSource() }

                RangeSlider(
                    value = priceRange,
                    onValueChange = { priceRange = it },
                    valueRange = 0f..5000f,
                    startInteractionSource = startInteractionSource,
                    endInteractionSource = endInteractionSource,
                    startThumb = {
                        Label(
                            label = {
                                PlainTooltip(
                                    modifier = Modifier
                                        .sizeIn(45.dp, 25.dp)
                                        .wrapContentWidth()
                                ) {
                                    Text("%.2f".format(it.activeRangeStart))
                                }
                            },
                            interactionSource = startInteractionSource
                        ) {
                            SliderDefaults.Thumb(startInteractionSource)
                        }
                    },
                    endThumb = {
                        Label(
                            label = {
                                PlainTooltip(
                                    modifier = Modifier
                                        .sizeIn(45.dp, 25.dp)
                                        .wrapContentWidth()
                                ) {
                                    Text("%.2f".format(it.activeRangeEnd))
                                }
                            },
                            interactionSource = endInteractionSource
                        ) {
                            SliderDefaults.Thumb(interactionSource = endInteractionSource)
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$%.2f".format(priceRange.start),
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "$%.2f".format(priceRange.endInclusive),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                LazyColumn(
                    modifier = Modifier
                ) {
                    items(availableCategories) { category ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = categories.contains(category),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        categories += category
                                    } else {
                                        categories -= category
                                    }
                                }
                            )

                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    )
}
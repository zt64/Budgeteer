package dev.zt64.budgeteer.ui.dialog

import androidx.compose.foundation.clickable
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
import dev.zt64.budgeteer.ui.model.SortBy
import dev.zt64.budgeteer.util.Formatter

@Composable
internal fun FilterDialog(
    filter: Filter,
    availableCategories: List<Category>,
    onDismissRequest: () -> Unit,
    onConfirm: (Filter) -> Unit
) {
    var categories by remember { mutableStateOf(filter.categories) }
    var priceRange by remember { mutableStateOf(filter.minAmount..filter.maxAmount) }
    var sortBy by remember { mutableStateOf(filter.sortBy) }

    Dialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            OutlinedButton(
                onClick = {
                    onConfirm(
                        filter.copy(
                            categories = categories,
                            minAmount = priceRange.start,
                            maxAmount = priceRange.endInclusive,
                            sortBy = sortBy
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
        }
    ) {
        Text(
            text = "Value Range",
            style = MaterialTheme.typography.titleLarge
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
                            Text(Formatter.formatMoney(it.activeRangeStart))
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
                            Text(Formatter.formatMoney(it.activeRangeEnd))
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
                text = Formatter.formatMoney(priceRange.start),
                style = MaterialTheme.typography.labelSmall
            )

            Text(
                text = Formatter.formatMoney(priceRange.endInclusive),
                style = MaterialTheme.typography.labelSmall
            )
        }

        Text(
            text = "Sort By",
            style = MaterialTheme.typography.titleLarge
        )

        SingleChoiceSegmentedButtonRow {
            SortBy.entries.forEachIndexed { index, entry ->
                SegmentedButton(
                    selected = sortBy == entry,
                    onClick = { sortBy = entry },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = SortBy.entries.size
                    )
                ) {
                    Text(entry.displayName)
                }
            }
        }

        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedCard {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                items(availableCategories) { category ->
                    var checked by remember { mutableStateOf(category in categories) }

                    Row(
                        modifier = Modifier
                            .clickable {
                                checked = !checked
                                if (checked) categories += category else categories -= category
                            }
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                if (isChecked) categories += category else categories -= category
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
}
package dev.zt64.budgeteer.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.ui.screen.HistoryScreen
import dev.zt64.budgeteer.ui.screen.HomeScreen
import dev.zt64.budgeteer.ui.theme.Theme

@Composable
fun Budgeteer() {
    Theme {
        var currentDestination by rememberSaveable { mutableStateOf(Destination.HOME) }

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                Destination.entries.forEach { entry ->
                    item(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        selected = currentDestination == entry,
                        icon = {
                            Icon(
                                imageVector = entry.icon,
                                contentDescription = entry.label
                            )
                        },
                        label = {
                            Text(entry.label)
                        },
                        onClick = {
                            currentDestination = entry
                        }
                    )
                }
            },
            layoutType = NavigationSuiteType.NavigationDrawer
        ) {
            when (currentDestination) {
                Destination.HOME -> {
                    HomeScreen()
                }
                Destination.HISTORY -> {
                    HistoryScreen()
                }
            }
        }
    }
}
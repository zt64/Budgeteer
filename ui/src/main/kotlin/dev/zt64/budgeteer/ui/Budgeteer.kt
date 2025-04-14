package dev.zt64.budgeteer.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zt64.budgeteer.ui.navigation.Destination
import dev.zt64.budgeteer.ui.navigation.NavHost
import dev.zt64.budgeteer.ui.navigation.rememberNavigationManager
import dev.zt64.budgeteer.ui.screen.HistoryScreen
import dev.zt64.budgeteer.ui.screen.HomeScreen
import dev.zt64.budgeteer.ui.screen.TransactionScreen
import dev.zt64.budgeteer.ui.theme.Theme

@Composable
fun Budgeteer() {
    Theme {
        val navigationManager = rememberNavigationManager(Destination.Home)

        NavHost(navigationManager) {
            NavigationSuiteScaffold(
                navigationSuiteItems = {
                    listOf(Destination.Home, Destination.History).forEach { entry ->
                        item(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            selected = navigationManager.currentDestination == entry,
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
                                navigationManager.navigate(entry)
                            }
                        )
                    }
                },
                layoutType = NavigationSuiteType.NavigationDrawer
            ) {
                when (val dest = navigationManager.currentDestination!!) {
                    Destination.Home -> HomeScreen()
                    Destination.History -> HistoryScreen()
                    is Destination.Transaction -> TransactionScreen(dest.id)
                }
            }
        }
    }
}
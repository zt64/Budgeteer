package dev.zt64.budgeteer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.zt64.budgeteer.ui.dialog.AboutDialog
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
            var showInfoDialog by remember { mutableStateOf(false) }

            if (showInfoDialog) {
                AboutDialog(onDismissRequest = { showInfoDialog = false })
            }

            Row {
                NavigationRail {
                    listOf(Destination.Home, Destination.History).forEach { entry ->
                        NavigationRailItem(
                            selected = navigationManager.currentDestination == entry,
                            icon = {
                                Icon(
                                    imageVector = entry.icon,
                                    contentDescription = entry.label
                                )
                            },
                            label = { Text(entry.label) },
                            onClick = { navigationManager.navigate(entry) }
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    NavigationRailItem(
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Info"
                            )
                        },
                        label = { Text("Info") },
                        onClick = { showInfoDialog = true }
                    )
                }

                Box {
                    val snackbarHostState = LocalSnackbarHostState.current

                    Surface {
                        when (val dest = navigationManager.currentDestination!!) {
                            Destination.Home -> HomeScreen()
                            is Destination.History -> HistoryScreen(dest.filter)
                            is Destination.Transaction -> TransactionScreen(dest.id)
                        }
                    }

                    SnackbarHost(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        hostState = snackbarHostState
                    )
                }
            }
        }
    }
}
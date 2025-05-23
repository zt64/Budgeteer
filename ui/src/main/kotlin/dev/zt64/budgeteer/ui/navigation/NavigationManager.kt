package dev.zt64.budgeteer.ui.navigation

import androidx.compose.runtime.*

internal val LocalNavigationManager = staticCompositionLocalOf<NavigationManager?> {
    error("No navigation manager provided")
}

@get:Composable
internal val ProvidableCompositionLocal<NavigationManager?>.currentOrThrow
    get() = current ?: throw IllegalStateException("No NavigationManager provided")

@Composable
internal fun rememberNavigationManager(initialDestination: Destination): NavigationManager {
    return remember { NavigationManager(initialDestination) }
}

@Composable
internal fun NavHost(manager: NavigationManager = rememberNavigationManager(Destination.Home), content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalNavigationManager provides manager, content = content)
}

@Stable
internal class NavigationManager(initialDestination: Destination) {
    private val _destinations = mutableStateListOf(initialDestination)
    val destinations: List<Destination> = _destinations

    val currentDestination: Destination?
        get() = _destinations.lastOrNull()

    fun navigate(destination: Destination) {
        if (currentDestination == destination) return

        _destinations.add(destination)
    }

    fun popBackStack(): Boolean {
        if (_destinations.size <= 1) return false
        _destinations.removeAt(_destinations.lastIndex)
        return true
    }

    fun navigateUp() {
        popBackStack()
    }
}
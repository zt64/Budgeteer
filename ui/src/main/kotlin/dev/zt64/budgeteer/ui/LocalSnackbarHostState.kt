package dev.zt64.budgeteer.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalSnackbarHostState = staticCompositionLocalOf { SnackbarHostState() }
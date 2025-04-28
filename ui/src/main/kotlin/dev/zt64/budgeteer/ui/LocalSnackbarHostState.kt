package dev.zt64.budgeteer.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf

internal val LocalSnackbarHostState = compositionLocalOf { SnackbarHostState() }
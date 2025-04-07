package dev.zt64.budgeteer.di

import dev.zt64.budgeteer.ui.viewmodel.HistoryViewModel
import dev.zt64.budgeteer.ui.viewmodel.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::HistoryViewModel)
}
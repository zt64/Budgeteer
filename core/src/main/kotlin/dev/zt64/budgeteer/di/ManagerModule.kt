package dev.zt64.budgeteer.di

import dev.zt64.budgeteer.domain.database.AppDatabase
import org.koin.dsl.module

val managerModule = module {
    single { AppDatabase.getDatabase() }
    single { get<AppDatabase>().transactionDao() }
    single { get<AppDatabase>().categoryDao() }
}
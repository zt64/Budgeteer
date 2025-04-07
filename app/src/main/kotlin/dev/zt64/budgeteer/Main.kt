package dev.zt64.budgeteer

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.singleWindowApplication
import dev.zt64.budgeteer.di.managerModule
import dev.zt64.budgeteer.di.viewModelModule
import dev.zt64.budgeteer.ui.Budgeteer
import org.koin.core.context.startKoin
import java.awt.Dimension

fun main() {
    startKoin {
        modules(managerModule, viewModelModule)
    }

    singleWindowApplication(title = "Budgeteer") {
        LaunchedEffect(Unit) {
            window.minimumSize = Dimension(400, 600)
        }

        Budgeteer()
    }
}
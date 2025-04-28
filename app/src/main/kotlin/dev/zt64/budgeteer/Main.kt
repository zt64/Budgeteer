package dev.zt64.budgeteer

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.singleWindowApplication
import dev.zt64.budgeteer.di.managerModule
import dev.zt64.budgeteer.di.repositoryModule
import dev.zt64.budgeteer.di.viewModelModule
import dev.zt64.budgeteer.ui.Budgeteer
import io.github.vinceglb.filekit.FileKit
import org.koin.core.context.startKoin
import java.awt.Dimension

fun main() {
    startKoin {
        modules(managerModule, repositoryModule, viewModelModule)
    }

    FileKit.init(appId = "Budgeteer")

    singleWindowApplication(title = "Budgeteer") {
        LaunchedEffect(Unit) {
            window.minimumSize = Dimension(800, 1200)
            window.maximumSize = Dimension(800, 1200)
            window.setLocationRelativeTo(null)
        }

        Budgeteer()
    }
}
@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("buildsrc.convention.kotlin-jvm")

    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
}

dependencies {
    implementation(project(":ui"))
    implementation(project(":core"))
    implementation(project(":utils"))

    implementation(compose.desktop.currentOs) {
        exclude(group = "org.jetbrains.compose.material", module = "material")
    }

    implementation(compose.material3AdaptiveNavigationSuite)

    implementation(libs.bundles.koin)
    implementation(libs.bundles.coroutines)

    testImplementation(compose.uiTest)
    testImplementation(libs.kotlin.test)
}

compose {
    desktop.application {
        mainClass = "dev.zt64.budgeteer.MainKt"

        nativeDistributions {
            packageName = "budgeteer"
            description = "Budgeteer is a simple budgeting application."
            vendor = "zt64"
            packageVersion = "1.0.0"

            targetFormats(TargetFormat.Deb, TargetFormat.Exe, TargetFormat.Msi)
        }
    }
}
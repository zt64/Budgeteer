@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("buildsrc.convention.kotlin-jvm")

    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

dependencies {
    ksp(libs.room.compiler)

    implementation(project(":utils"))

    implementation(compose.desktop.currentOs) {
        exclude(group = "org.jetbrains.compose.material", module = "material")
    }

    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(compose.material3AdaptiveNavigationSuite)

    implementation(libs.bundles.koin)
    implementation(libs.bundles.coroutines)

    implementation(libs.room.runtime)
    implementation(libs.sqlite.bundled)

    testImplementation(compose.uiTest)
    testImplementation(libs.kotlin.test)
}

room {
    schemaDirectory("$projectDir/schemas")
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
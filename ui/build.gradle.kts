@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("buildsrc.convention.kotlin-jvm")

    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
}

kotlin {
    compilerOptions {
        optIn.addAll("androidx.compose.material3.ExperimentalMaterial3Api", "kotlin.time.ExperimentalTime")
    }
}

dependencies {
    implementation(project(":core"))

    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(compose.material3AdaptiveNavigationSuite)

    implementation(libs.bundles.koin)
    implementation(libs.bundles.coroutines)

    implementation(libs.koalaPlot.core)

    testImplementation(compose.uiTest)
    testImplementation(libs.kotlin.test)
}
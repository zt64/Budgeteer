@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("buildsrc.convention.kotlin-jvm")

    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
}

dependencies {
    implementation(project(":core"))

    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(compose.material3AdaptiveNavigationSuite)

    implementation(libs.bundles.koin)
    implementation(libs.bundles.coroutines)

    testImplementation(compose.uiTest)
    testImplementation(libs.kotlin.test)
}
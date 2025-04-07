plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.bundles.kotlinxEcosystem)
    testImplementation(libs.kotlin.test)
}
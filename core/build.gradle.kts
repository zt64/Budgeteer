plugins {
    id("buildsrc.convention.kotlin-jvm")

    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

dependencies {
    ksp(libs.room.compiler)

    implementation(project(":utils"))

    implementation(libs.bundles.koin)
    implementation(libs.bundles.coroutines)

    implementation(libs.room.runtime)
    implementation(libs.sqlite.bundled)

    testImplementation(libs.kotlin.test)
}

room {
    schemaDirectory("$projectDir/schemas")
}
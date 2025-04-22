plugins {
    id("buildsrc.convention.kotlin-jvm")

    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    compilerOptions {
        optIn.add("kotlin.time.ExperimentalTime")
    }
}

dependencies {
    ksp(libs.room.compiler)

    implementation(project(":utils"))

    implementation(libs.bundles.koin)
    implementation(libs.bundles.coroutines)

    implementation(libs.room.runtime)
    implementation(libs.sqlite.bundled)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.room.testing)
}

room {
    schemaDirectory("$projectDir/schemas")
}
plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.plugins.kotlin.jvm)
}

fun DependencyHandler.implementation(dependency: Provider<PluginDependency>) {
    implementation(dependency.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" })
}
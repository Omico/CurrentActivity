plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(androidGradlePlugin)
    implementation(com.diffplug.spotless)
    implementation(consensusGradlePlugins)
    implementation(gradmGeneratedJar)
    implementation(kotlinGradlePlugin)
    implementation(org.jetbrains.kotlin.plugin.compose)
}

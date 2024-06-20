plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(androidGradlePlugin)
    implementation(com.diffplug.spotless)
    implementation(gradmGeneratedJar)
    implementation(kotlinGradlePlugin)
    implementation(me.omico.consensus.api)
    implementation(me.omico.consensus.dsl)
    implementation(me.omico.consensus.git)
    implementation(me.omico.consensus.spotless)
    implementation(org.jetbrains.kotlin.plugin.compose)
}

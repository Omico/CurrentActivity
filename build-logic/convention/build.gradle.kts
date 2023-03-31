plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(androidGradlePlugin)
    implementation(com.diffplug.spotless)
    implementation(gradmGeneratedJar)
    implementation(kotlinGradlePlugin)
    implementation(me.omico.age.dsl)
    implementation(me.omico.age.spotless)
}

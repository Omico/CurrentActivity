plugins {
    `kotlin-dsl`
    id("me.omico.gradm") version "4.0.0-beta03"
    id("me.omico.gradm.integration.github") version "4.0.0-beta03"
}

repositories {
    mavenCentral()
}

gradm {
    pluginId = "ca.gradm"
    debug = true
}

@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

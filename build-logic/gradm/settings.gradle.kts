@file:Suppress("UnstableApiUsage")

rootProject.name = "gradm"

pluginManagement {
    includeBuild("../initialization")
    repositories {
        mavenCentral()
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

plugins {
    id("initialization")
}

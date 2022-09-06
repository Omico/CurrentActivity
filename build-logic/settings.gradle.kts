@file:Suppress("UnstableApiUsage")

rootProject.name = "build-logic"

pluginManagement {
    includeBuild("initialization")
    repositories {
        mavenCentral()
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

plugins {
    id("initialization.gradm")
}

include(":convention")

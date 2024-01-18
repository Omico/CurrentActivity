@file:Suppress("UnstableApiUsage")

rootProject.name = "build-logic"

pluginManagement {
    includeBuild("initialization")
    includeBuild("gradm")
    repositories {
        mavenCentral()
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

plugins {
    id("initialization")
    id("ca.gradm")
}

include(":convention")

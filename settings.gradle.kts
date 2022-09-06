@file:Suppress("UnstableApiUsage")

rootProject.name = "CurrentActivity"

pluginManagement {
    includeBuild("build-logic")
    includeBuild("build-logic/initialization")
    repositories {
        gradlePluginPortal {
            content {
                includeGroupByRegex("com.gradle.*")
            }
        }
        mavenCentral()
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

plugins {
    id("initialization")
    id("com.gradle.enterprise") version "3.11.1"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlways()
    }
}

include(":app")
include(":core")
include(":core:common")
include(":core:common:resources")
include(":core:shizuku")
include(":core:shizuku:hidden-api")
include(":core:monitor")
include(":data")
include(":ui")
include(":ui:common")
include(":ui:common:theme")
include(":ui:home")
include(":ui:main")
include(":ui:monitor")

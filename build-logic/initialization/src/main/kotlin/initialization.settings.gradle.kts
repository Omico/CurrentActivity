@file:Suppress("UnstableApiUsage")

import me.omico.age.dsl.ageSnapshots
import me.omico.age.dsl.gradmSnapshots
import me.omico.age.dsl.spotless

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        ageSnapshots()
        google()
        gradmSnapshots()
        mavenCentral()
        spotless()
    }
}

dependencyResolutionManagement {
    repositories {
        ageSnapshots()
        google()
        gradmSnapshots()
        mavenCentral()
        spotless()
    }
}

buildCache {
    local {
        removeUnusedEntriesAfterDays = 1
    }
}

plugins {
    id("me.omico.age.settings.initialization")
}

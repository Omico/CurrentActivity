import me.omico.consensus.root.consensusRootProjectConfiguration
import me.omico.gradle.project.PROJECT_JAVA_VERSION

plugins {
    kotlin("android")
    id("com.android.application")
    id("ca.android.base")
}

android {
    defaultConfig {
        versionCode = consensusRootProjectConfiguration.versionCode
        versionName = consensusRootProjectConfiguration.versionName
    }
    buildFeatures {
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = PROJECT_JAVA_VERSION.toString()
    }
    buildTypes {
        release {
            @Suppress("UnstableApiUsage")
            vcsInfo.include = false
        }
    }
}

androidComponents {
    onVariants(selector().withBuildType("release")) { variant ->
        variant.packaging.resources.excludes.addAll(
            "DebugProbesKt.bin",
            "META-INF/*.version",
            "kotlin-tooling-metadata.json",
        )
    }
}

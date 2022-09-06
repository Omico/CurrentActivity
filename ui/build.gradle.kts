plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    buildFeatures {
        androidResources = false
        buildConfig = false
        compose = true
    }
}

dependencies {
    api(projects.ui.common)
    api(projects.ui.main)
}

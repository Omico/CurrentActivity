plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    buildFeatures {
        buildConfig = false
        compose = true
    }
}

dependencies {
    implementation(projects.ui.common)
    implementation(projects.ui.home)
    implementation(projects.ui.monitor)
}

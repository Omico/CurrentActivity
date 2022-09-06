plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.ui.common)
}

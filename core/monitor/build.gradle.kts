plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    compileOnly(projects.core.common)
}

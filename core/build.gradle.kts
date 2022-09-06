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
    api(projects.core.common)
    api(projects.core.shizuku)
    api(projects.core.monitor)
}

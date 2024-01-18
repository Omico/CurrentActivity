plugins {
    kotlin("android")
    id("com.android.application")
    id("ca.android.base")
    id("ca.android.compose")
}

android {
    defaultConfig {
        versionCode = version.toString().replace(".", "").toInt()
        versionName = version.toString()
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

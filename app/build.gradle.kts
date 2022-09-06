plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    defaultConfig {
        versionCode = 1
        versionName = "2022.0.0"
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.ui)
}

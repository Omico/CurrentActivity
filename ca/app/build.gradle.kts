plugins {
    id("ca.android.application")
    id("ca.compose-compiler")
}

android {
    signingConfigs {
        named("debug") {
            storeFile = file("debug.keystore")
        }
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            signingConfig = signingConfigs["debug"]
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(project(":ca-ui"))
}

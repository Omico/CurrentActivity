import com.android.builder.signing.DefaultSigningConfig

plugins {
    id("ca.android.application")
}

android {
    signingConfigs {
        named("debug") {
            storeFile = file("debug.keystore")
            storePassword = DefaultSigningConfig.DEFAULT_PASSWORD
            keyAlias = DefaultSigningConfig.DEFAULT_ALIAS
            keyPassword = DefaultSigningConfig.DEFAULT_PASSWORD
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

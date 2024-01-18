plugins {
    id("ca.android.library")
    id("ca.android.compose")
}

android {
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":ca-ui-common"))
}

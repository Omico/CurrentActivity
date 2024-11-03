plugins {
    id("ca.android.library")
    id("ca.compose-compiler")
}

android {
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":ca-ui-common"))
}

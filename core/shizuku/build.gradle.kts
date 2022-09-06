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

dependencies {
    implementation(projects.core.shizuku.hiddenApi)
}

dependencies {
    api(shizuku.api)
    implementation(shizuku.provider)
}

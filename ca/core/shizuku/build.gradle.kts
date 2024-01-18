plugins {
    id("ca.android.library")
}

dependencies {
    implementation(project(":ca-core-common"))
    implementation(project(":ca-core-shizuku-hidden-api"))
}

dependencies {
    api(shizuku.api)
    implementation(shizuku.provider)
}

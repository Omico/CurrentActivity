plugins {
    id("ca.android.library")
    id("ca.compose-compiler")
}

dependencies {
    implementation(project(":ca-ui-common"))
    implementation(project(":ca-ui-home"))
    implementation(project(":ca-ui-monitor"))
}

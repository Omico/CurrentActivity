plugins {
    id("ca.android.library")
    id("ca.android.compose")
}

dependencies {
    api(project(":ca-ui-common"))
    api(project(":ca-ui-main"))
}

plugins {
    id("ca.android.library")
    id("ca.compose-compiler")
}

dependencies {
    api(project(":ca-ui-common"))
    api(project(":ca-ui-main"))
}

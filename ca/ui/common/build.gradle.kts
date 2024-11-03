plugins {
    id("ca.android.library")
    id("ca.compose-compiler")
}

dependencies {
    api(project(":ca-core"))
    api(project(":ca-data"))
    api(project(":ca-ui-common-theme"))
}

dependencies {
    api(accompanist.swipeRefresh)
    api(androidx.activity.compose)
    api(androidx.compose.material.icons.extended)
    api(androidx.compose.material3)
    api(androidx.compose.material3.windowSizeClass)
    api(androidx.compose.ui)
    api(androidx.compose.ui.tooling.preview)
    api(androidx.navigation.compose)
    debugApi(androidx.compose.ui.tooling)
}

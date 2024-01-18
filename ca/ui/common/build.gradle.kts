plugins {
    id("ca.android.library")
    id("ca.android.compose")
}

dependencies {
    api(project(":ca-core"))
    api(project(":ca-data"))
    api(project(":ca-ui-common-theme"))
}

dependencies {
    api(accompanist.swipeRefresh)
    api(accompanist.systemUiController)
    api(androidx.activity.compose)
    api(androidx.compose.animation)
    api(androidx.compose.foundation)
    api(androidx.compose.material.icons.core)
    api(androidx.compose.material.icons.extended)
    api(androidx.compose.material3)
    api(androidx.compose.material3.windowSizeClass)
    api(androidx.compose.runtime)
    api(androidx.compose.ui)
    api(androidx.compose.ui.tooling.preview)
    api(androidx.compose.ui.util)
    api(androidx.navigation.compose)
    api(androidx.navigation.runtime.ktx)
    debugApi(androidx.compose.ui.tooling)
}

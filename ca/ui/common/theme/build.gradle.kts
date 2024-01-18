plugins {
    id("ca.android.library")
    id("ca.android.compose")
}

dependencies {
    api(project(":ca-core-common-resources"))
}

dependencies {
    api(androidx.core.splashScreen)
    api(material)
    compileOnly(accompanist.systemUiController)
    compileOnly(androidx.compose.material3)
}

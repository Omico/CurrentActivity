plugins {
    id("ca.android.library")
    id("ca.compose-compiler")
}

dependencies {
    api(project(":ca-core-common-resources"))
}

dependencies {
    api(androidx.core.splashScreen)
    api(material)
    compileOnly(androidx.compose.material3)
}

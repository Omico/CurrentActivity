plugins {
    id("ca.android.library")
}

dependencies {
    api(project(":ca-core-common-resources"))
}

dependencies {
    api(androidx.activity.ktx)
    api(androidx.annotation)
    api(androidx.core.ktx)
    api(androidx.lifecycle.runtime.ktx)
    api(androidx.lifecycle.service)
    api(androidx.savedState.ktx)
    api(kotlinx.coroutines.android)
    implementation(hiddenApiBypass)
}

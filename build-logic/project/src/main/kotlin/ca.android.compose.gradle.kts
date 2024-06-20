import me.omico.gradle.project.configureCommonAndroidCompose

plugins {
    id("com.android.base")
    kotlin("plugin.compose")
}

configureCommonAndroidCompose()

composeCompiler {
    metricsDestination = file("build/composeMetrics")
    reportsDestination = file("build/composeReports")
}

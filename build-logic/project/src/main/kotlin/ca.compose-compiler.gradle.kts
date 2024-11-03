plugins {
    kotlin("plugin.compose")
}

composeCompiler {
    metricsDestination = file("build/composeMetrics")
    reportsDestination = file("build/composeReports")
}

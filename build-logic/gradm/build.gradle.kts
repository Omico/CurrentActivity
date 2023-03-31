buildscript {
    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
    }
}

plugins {
    `kotlin-dsl`
    id("me.omico.gradm") version "3.1.0"
}

gradm {
    pluginId = "gradm"
    debug = true
    integrations {
        apply("github")
    }
}

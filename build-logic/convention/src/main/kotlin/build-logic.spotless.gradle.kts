import me.omico.age.spotless.configureSpotless
import me.omico.age.spotless.intelliJIDEARunConfiguration
import me.omico.age.spotless.kotlin
import me.omico.age.spotless.kotlinGradle

plugins {
    id("build-logic.root-project.base")
    id("com.diffplug.spotless")
    id("me.omico.age.spotless")
}

allprojects {
    configureSpotless {
        intelliJIDEARunConfiguration()
        kotlin(
            licenseHeaderFile = rootProject.file("spotless/copyright.kt").takeIf(File::exists),
            licenseHeaderConfig = {
                updateYearWithLatest(true)
                yearSeparator("-")
            },
        )
        kotlinGradle()
    }
}

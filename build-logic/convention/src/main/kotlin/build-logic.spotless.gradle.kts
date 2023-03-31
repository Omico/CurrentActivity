import me.omico.age.spotless.configureSpotless
import me.omico.age.spotless.defaultEditorConfig
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
            editorConfig = mutableMapOf<String, String>().apply {
                putAll(defaultEditorConfig)
                put("ktlint_standard_annotation", "disabled")
            },
            licenseHeaderFile = rootProject.file("spotless/copyright.kt").takeIf(File::exists),
            licenseHeaderConfig = {
                updateYearWithLatest(true)
                yearSeparator("-")
            },
        )
        kotlinGradle()
    }
}

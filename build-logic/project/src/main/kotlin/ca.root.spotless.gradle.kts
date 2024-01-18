import me.omico.consensus.dsl.requireRootProject
import me.omico.consensus.spotless.ConsensusSpotlessTokens

plugins {
    id("me.omico.consensus.spotless")
}

requireRootProject()

consensus {
    spotless {
        rootProject {
            freshmark()
            gradleProperties()
        }
        allprojects {
            kotlin(
                targets = ConsensusSpotlessTokens.Kotlin.targets + setOf(
                    "build-logic/**/src/main/kotlin/**/*.kt",
                    "build-logic/**/src/main/kotlin/**/*.kts",
                ),
            )
            kotlinGradle(
                targets = ConsensusSpotlessTokens.KotlinGradle.targets + setOf(
                    "build-logic/*/*.gradle.kts",
                ),
            )
        }
    }
}

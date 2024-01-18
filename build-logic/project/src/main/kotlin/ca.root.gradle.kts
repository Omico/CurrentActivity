import me.omico.consensus.dsl.requireRootProject

plugins {
    id("ca.gradm")
    id("ca.root.git")
    id("ca.root.spotless")
}

requireRootProject()

consensus {
    allprojects {
        group = gradleProperty("project.group")
        version = gradleProperty("project.version")
    }
}

val wrapper: Wrapper by tasks.named<Wrapper>("wrapper") {
    gradleVersion = versions.gradle
    distributionType = Wrapper.DistributionType.BIN
}

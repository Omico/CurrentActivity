plugins {
    id("me.omico.consensus.root")
    id("ca.gradm")
    id("ca.root.git")
    id("ca.root.spotless")
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = versions.gradle
    distributionType = Wrapper.DistributionType.BIN
}

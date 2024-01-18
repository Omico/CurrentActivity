import me.omico.gradle.initialization.includeAllSubprojectModules
import me.omico.gradm.addDeclaredRepositories

addDeclaredRepositories()

plugins {
    id("ca.gradm")
    id("ca.gradle-enterprise")
}

includeBuild("build-logic/project")

includeAllSubprojectModules("ca")

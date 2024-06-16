import me.omico.gradle.initialization.includeAllSubprojectModules
import me.omico.gradm.addDeclaredRepositories

addDeclaredRepositories()

plugins {
    id("ca.develocity")
    id("ca.gradm")
}

includeBuild("build-logic/project")

includeAllSubprojectModules("ca")

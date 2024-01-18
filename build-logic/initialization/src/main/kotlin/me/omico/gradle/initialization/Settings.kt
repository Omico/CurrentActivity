package me.omico.gradle.initialization

import org.gradle.api.initialization.Settings
import java.io.File

internal fun Settings.includeAllSubprojectModules(projectName: String): Unit =
    rootDir.resolve(projectName).walk()
        .filter(File::isDirectory)
        .filter { it.resolve("build.gradle.kts").exists() }
        .forEach { moduleDirectory ->
            val moduleName = moduleDirectory.relativeTo(rootDir).path.replace(File.separator, "-")
            include(":$moduleName")
            project(":$moduleName").projectDir = moduleDirectory
        }

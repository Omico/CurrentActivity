/*
 * This file is part of CurrentActivity.
 *
 * Copyright (C) 2024 Omico
 *
 * CurrentActivity is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * CurrentActivity is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CurrentActivity. If not, see <https://www.gnu.org/licenses/>.
 */
package me.omico.gradle.project

import com.android.build.api.AndroidPluginVersion
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal typealias AndroidCommonExtension = CommonExtension<*, *, *, *, *, *>

internal fun Project.configureAndroidCommonExtension(block: AndroidCommonExtension.() -> Unit): Unit =
    extensions.configure("android", block)

internal fun Project.configureAndroidCommonExtension(
    domain: String,
    compileSdk: Int,
    minSdk: Int,
    namespace: String = "$domain.${name.replace("-", ".")}",
    javaVersion: JavaVersion = JavaVersion.toVersion(PROJECT_JAVA_VERSION),
    coreLibraryDesugaringDependency: Any,
) {
    checkMinimalSupportedAndroidGradlePluginVersion()
    configureAndroidCommonExtension {
        this.namespace = namespace
        this.compileSdk = compileSdk
        defaultConfig {
            this.minSdk = minSdk
        }
        compileOptions {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
            isCoreLibraryDesugaringEnabled = true
        }
    }
    dependencies.add("coreLibraryDesugaring", coreLibraryDesugaringDependency)
}

@Suppress("MagicNumber")
private val MinimalSupportedAndroidPluginVersion = AndroidPluginVersion(8, 0)

private fun checkMinimalSupportedAndroidGradlePluginVersion(): Unit =
    require(AndroidPluginVersion.getCurrent() >= MinimalSupportedAndroidPluginVersion) {
        "Minimal supported Android Gradle Plugin version is 8.0.0"
    }

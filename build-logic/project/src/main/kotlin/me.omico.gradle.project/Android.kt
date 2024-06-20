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

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureCommonAndroid(
    compileSdk: Int,
    minSdk: Int,
    domain: String = "me.omico",
    namespace: String = "$domain.${name.replace("-", ".")}",
    coreLibraryDesugaringVersion: String = "2.0.4",
) {
    commonAndroid {
        this.namespace = namespace
        this.compileSdk = compileSdk
        defaultConfig {
            this.minSdk = minSdk
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
            isCoreLibraryDesugaringEnabled = true
        }
    }
    dependencies.add(
        "coreLibraryDesugaring",
        "com.android.tools:desugar_jdk_libs:$coreLibraryDesugaringVersion",
    )
}

internal fun Project.configureCommonAndroidCompose() {
    commonAndroid {
        buildFeatures {
            compose = true
        }
    }
}

private fun Project.commonAndroid(action: CommonExtension<*, *, *, *, *, *>.() -> Unit) =
    extensions.configure("android", action)

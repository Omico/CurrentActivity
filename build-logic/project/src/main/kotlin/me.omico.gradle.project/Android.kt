/*
 * Copyright 2024 Omico. All Rights Reserved.
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

internal fun Project.configureCommonAndroidCompose(
    composeCompilerVersion: String,
) {
    commonAndroid {
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = composeCompilerVersion
        }
    }
}

private fun Project.commonAndroid(action: CommonExtension<*, *, *, *, *, *>.() -> Unit) =
    extensions.configure("android", action)

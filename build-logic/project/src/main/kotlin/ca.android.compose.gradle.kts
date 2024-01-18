import me.omico.gradle.project.configureCommonAndroidCompose
import me.omico.gradm.Versions

plugins {
    id("com.android.base")
}

configureCommonAndroidCompose(
    composeCompilerVersion = Versions.androidx.compose.compiler,
)

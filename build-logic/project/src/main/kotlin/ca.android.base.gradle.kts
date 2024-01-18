import me.omico.gradle.project.configureCommonAndroid

plugins {
    id("com.android.base")
}

configureCommonAndroid(
    compileSdk = 34,
    minSdk = 30,
    namespace = "me.omico.${
        name
            .replace("-", ".")
            .replace("ca", "currentactivity")
            .removeSuffix(".app")
    }",
)

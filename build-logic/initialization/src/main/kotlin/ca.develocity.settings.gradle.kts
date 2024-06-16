plugins {
    id("com.gradle.develocity")
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
        publishing {
            val isOffline = providers.provider { gradle.startParameter.isOffline }
            onlyIf { !isOffline.getOrElse(false) }
        }
    }
}

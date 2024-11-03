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
        obfuscation {
            val isCi = providers.environmentVariable("CI").getOrElse("false").toBooleanStrict()
            if (isCi) return@obfuscation
            username { "hidden" }
            hostname { "hidden" }
            ipAddresses { listOf("0.0.0.0") }
            externalProcessName { "non-build-process" }
        }
    }
}

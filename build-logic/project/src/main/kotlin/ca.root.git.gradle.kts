import me.omico.consensus.api.dsl.requireRootProject

plugins {
    id("me.omico.consensus.git")
}

requireRootProject()

consensus {
    git {
        hooks {
            preCommit {
                appendLine("#!/bin/sh")
                appendLine()
                correctGradleWrapperScriptPermissions()
                appendLine()
                checkGitAttributes()
                appendLine()
            }
        }
    }
}

fun StringBuilder.correctGradleWrapperScriptPermissions() {
    if (!file("gradlew").exists()) return
    appendLine("# Give gradlew execute permission")
    appendLine("git ls-files \"*gradlew\" | xargs git update-index --add --chmod=+x")
}

fun StringBuilder.checkGitAttributes() {
    if (!file(".gitattributes").exists()) return
    appendLine("#  Check for missing attributes")
    appendLine("missing_attributes=\$(git ls-files | git check-attr -a --stdin | grep 'text: auto' || printf '\\n')")
    appendLine("if [ -n \"\$missing_attributes\" ]; then")
    appendLine("    echo 'Some files are missing the text attribute. Please add the following to .gitattributes:'")
    appendLine("    echo \"\$missing_attributes\" | sed 's/: text: auto//g' | sed 's/^/    /'")
    appendLine("    exit 1")
    appendLine("fi")
}

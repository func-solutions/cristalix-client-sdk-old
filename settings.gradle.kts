@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    plugins {
        kotlin("jvm") version "1.7.0"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://repo.c7x.ru/repository/maven-public/")
            credentials {
                username = System.getenv("CRI_REPO_LOGIN") ?: System.getenv("CRISTALIX_REPO_USERNAME")
                password = System.getenv("CRI_REPO_PASSWORD") ?: System.getenv("CRISTALIX_REPO_PASSWORD")
            }
        }
    }

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

rootProject.name = "cristalix-client-sdk"

include(
    "client-api",
    "client-sdk",
    "ui-engine"
)

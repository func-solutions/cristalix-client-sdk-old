import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    `java-library`
    kotlin("jvm") apply false
}

allprojects {
    group = "ru.cristalix"
    version = "8.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(8))
        withSourcesJar()
    }

    tasks {
        withType<Jar>().configureEach { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }
        withType<JavaCompile>().configureEach { options.encoding = "UTF-8" }
        withType<KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = listOf(
                    "-Xlambdas=indy",
                    "-Xno-param-assertions",
                    "-Xno-receiver-assertions",
                    "-Xno-call-assertions",
                    "-Xbackend-threads=0",
                    "-Xuse-k2",
                    "-Xassertions=always-disable",
                    "-Xuse-fast-jar-file-system",
                    "-Xsam-conversions=indy"
                )
            }
        }
    }

    publishing {
        repositories {
            mavenLocal()
            maven {
                name = "c7x"
                url = uri(
                    "https://repo.c7x.ru/repository/maven-${
                        if (project.version.toString().contains("SNAPSHOT")) "snapshots" else "releases"
                    }/"
                )
                credentials {
                    username = System.getenv("CRI_REPO_LOGIN") ?: System.getenv("CRISTALIX_REPO_USERNAME")
                    password = System.getenv("CRI_REPO_PASSWORD") ?: System.getenv("CRISTALIX_REPO_PASSWORD")
                }
            }
        }
    }
}

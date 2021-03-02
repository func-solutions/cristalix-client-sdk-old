plugins {
    kotlin("jvm") version "1.4.21"
    `maven-publish`
}

group = "ru.cristalix"

subprojects {

    apply("plugin" to "maven-publish")
    apply("plugin" to "kotlin")

    repositories {
        mavenCentral()
//        maven {
//            setUrl("https://repo.implario.dev/public")
//        }
        maven {
            setUrl("https://repository.anfanik.me/public/")
        }
        maven {
            setUrl("https://libraries.minecraft.net/")
        }
    }

    dependencies {
        compileOnly(kotlin("stdlib"))
    }

    tasks {
        jar {
//            from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
        compileKotlin {
            kotlinOptions.jvmTarget = "1.6"
            kotlinOptions.noStdlib = false
            kotlinOptions.noReflect = true
            kotlinOptions.freeCompilerArgs += "-Xno-param-assertions"
            kotlinOptions.freeCompilerArgs += "-Xno-call-assertions"
            kotlinOptions.freeCompilerArgs += "-Xno-receiver-assertions"
            kotlinOptions.freeCompilerArgs += "-Xassertions=always-disable"
//            kotlinOptions.noJdk = true
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = "1.6"
            kotlinOptions.noStdlib = false
            kotlinOptions.noReflect = true
            kotlinOptions.freeCompilerArgs += "-Xno-param-assertions"
            kotlinOptions.freeCompilerArgs += "-Xno-call-assertions"
//            kotlinOptions.noJdk = true
        }
    }


    afterEvaluate {
        publishing {
            publications {
                val artifactName = when (project.name) {
                    "bundler-gradle-plugin" -> "bundler"
                    "engine" -> "uiengine"
                    else -> project.name
                }
                val publicationName = "-[a-zA-Z]".toRegex().replace(artifactName) {
                    it.value.replace("-", "").toUpperCase()
                }
                create<MavenPublication>(publicationName) {
                    groupId = "ru.cristalix"
                    artifactId = artifactName
                    version = when (project.name) {
                        "bundler-gradle-plugin" -> "2.1.5"
                        "engine" -> "3.5.3"
                        "client-api-libs" -> "all"
                        else -> "1.0"
                    }

                    if (project.name == "bundler-gradle-plugin") from(project.components["java"])
                    else artifact(project.tasks.jar.get())

                }
            }
            repositories {
                maven {
                    name = "implario"
                    setUrl("https://repo.implario.dev/public")
                    credentials {
                        username = System.getenv("IMPLARIO_REPO_USER")
                        password = System.getenv("IMPLARIO_REPO_PASSWORD")
                    }
                }
            }
        }

    }

}



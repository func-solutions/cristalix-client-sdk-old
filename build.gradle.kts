plugins {
    kotlin("jvm") version "1.4.21"
    `maven-publish`
}

group = "ru.cristalix"

allprojects {

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
        if (project.name == "bundler-gradle-plugin" || project.name == "engine" || project.name == "client-api-full") {
            publishing {
                publications {
                    create<MavenPublication>("maven") {
                        groupId = "ru.cristalix"
                        artifactId = when (project.name) {
                            "bundler-gradle-plugin" -> "bundler"
                            "engine" -> "uiengine"
                            else -> project.name
                        }
                        version = when (project.name) {
                            "bundler-gradle-plugin" -> "2.1.5"
                            "engine" -> "3.3.5"
                            "client-api-full" -> "1.0.2"
                            else -> "1.0"
                        }

                        if (project.name == "bundler-gradle-plugin") from(project.components["java"])
                        else artifact(project.tasks.jar.get())

                    }
                }
                repositories {
                    maven {
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

}



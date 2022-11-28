plugins {
    `maven-publish`
    `java-library`
}

//allprojects {
//    group = "ru.cristalix"
//}

subprojects {

    apply("plugin" to "maven-publish")

    repositories {
        maven {
            setUrl("https://repo.c7x.ru/repository/maven-public/")
            credentials {
                username = System.getenv("REPO_C7X_USERNAME") ?: System.getenv("CRI_REPO_LOGIN") ?: project.properties["CRI_REPO_LOGIN"] as String
                password = System.getenv("REPO_C7X_PASSWORD") ?: System.getenv("CRI_REPO_PASSWORD") ?: project.properties["CRI_REPO_PASSWORD"] as String
            }
        }
        mavenCentral()
    }


    afterEvaluate {
        tasks.jar {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        }
        publishing {
            repositories {
                maven {
                    setUrl("https://repo.c7x.ru/repository/private-anelfer/")
                    credentials {
                        username = System.getenv("REPO_C7X_USERNAME") ?: System.getenv("CRI_REPO_LOGIN") ?: project.properties["CRI_REPO_LOGIN"] as String
                        password = System.getenv("REPO_C7X_PASSWORD") ?: System.getenv("CRI_REPO_PASSWORD") ?: project.properties["CRI_REPO_PASSWORD"] as String
                    }
                }
            }
        }
    }
}



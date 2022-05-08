import org.gradle.internal.impldep.org.bouncycastle.cms.RecipientId.password

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
                username = System.getenv("CRI_REPO_LOGIN") ?: project.properties["CRI_REPO_LOGIN"] as String
                password = System.getenv("CRI_REPO_PASSWORD") ?: project.properties["CRI_REPO_PASSWORD"] as String
            }
        }
        mavenCentral()
    }


    afterEvaluate {
        tasks.jar {
            from(sourceSets.main.get().allSource)
        }
        publishing {
            repositories {
                maven {
                    setUrl("https://repo.c7x.ru/repository/maven-releases/")
                    credentials {
                        username = System.getenv("CRI_REPO_LOGIN") ?: project.properties["CRI_REPO_LOGIN"] as String
                        password = System.getenv("CRI_REPO_PASSWORD") ?: project.properties["CRI_REPO_PASSWORD"] as String
                    }
                }
            }
        }
    }
}



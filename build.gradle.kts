plugins {
    `maven-publish`
    java
}

group = "ru.cristalix"

subprojects {

    apply("plugin" to "maven-publish")

    repositories {
        maven {
            setUrl("https://repo.implario.dev/public")
        }
        mavenCentral()
    }


    afterEvaluate {
        publishing {
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



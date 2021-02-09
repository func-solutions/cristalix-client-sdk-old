plugins {
    `maven-publish`
}

publishing {

    repositories {
        maven {
            name = "implario"
            setUrl("https://repo.implario.dev/public/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("uiengine") {
            groupId = "ru.cristalix"
            artifactId = "uiengine"
            version = rootProject.version.toString()
            from(components["java"])
        }
    }
}

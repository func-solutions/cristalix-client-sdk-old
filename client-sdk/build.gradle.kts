plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.30"
}

group = "ru.cristalix"

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(project(":client-api"))
}

publishing {
    publications {
        create<MavenPublication>("clientSdk") {
            groupId = "ru.cristalix"
            artifactId = "client-sdk"
            version = "5.1.0"
            from(components["java"])
        }
    }
}


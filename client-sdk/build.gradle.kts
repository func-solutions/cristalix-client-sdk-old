plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.30"
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(project(":client-api"))
}

publishing {
    publications {
        create<MavenPublication>("clientSdk") {
            groupId = "ru.cristalix"
            artifactId = "client-sdk"
            version = "5.0.3-pre8"
            from(components["java"])
        }
    }
}


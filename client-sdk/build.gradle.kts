plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.30"
}

group = "ru.cristalix"

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(project(":client-api"))
}

tasks.compileKotlin.get().kotlinOptions.jvmTarget = "1.8"

publishing {
    publications {
        create<MavenPublication>("clientSdk") {
            groupId = "ru.cristalix"
            artifactId = "client-sdk"
            version = "5.4.1"
            from(components["java"])
        }
    }
}

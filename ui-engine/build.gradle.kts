plugins {
    kotlin("jvm") version "1.4.21"
}

apply("plugin" to "kotlin")

group = "ru.cristalix"

dependencies {
    compileOnly(project(":client-api"))
    compileOnly(project(":client-sdk"))
    compileOnly(kotlin("stdlib"))

}

tasks.compileKotlin.get().kotlinOptions.jvmTarget = "1.8"

publishing {
    publications {
        create<MavenPublication>("uiengine") {
            groupId = "ru.cristalix"
            artifactId = "uiengine"
            version = "3.9.7"
            from(components["java"])
        }
    }
}


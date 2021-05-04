plugins {
    kotlin("jvm") version "1.4.21"
}

apply("plugin" to "kotlin")

dependencies {
    compileOnly(project(":client-api"))
    compileOnly(project(":client-sdk"))
    compileOnly(kotlin("stdlib"))

}

publishing {
    publications {
        create<MavenPublication>("uiengine") {
            groupId = "ru.cristalix"
            artifactId = "uiengine"
            version = "3.7.7-rc1"
            from(components["java"])
        }
    }
}


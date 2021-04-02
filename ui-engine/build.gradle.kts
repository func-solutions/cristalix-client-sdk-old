plugins {
    kotlin("jvm") version "1.4.21"
}

apply("plugin" to "kotlin")

dependencies {
    compileOnly(project(":client-api-libs"))
    compileOnly(kotlin("stdlib"))

}

publishing {
    publications {
        create<MavenPublication>("uiengine") {
            groupId = "ru.cristalix"
            artifactId = "uiengine"
            version = "3.6.2"
            from(components["java"])
        }
    }
}


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

tasks.compileJava.get().run {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}
tasks.compileKotlin.get().kotlinOptions.jvmTarget = "1.8"

publishing {
    publications {
        create<MavenPublication>("uiengine") {
            groupId = "ru.cristalix"
            artifactId = "uiengine"
            version = "3.13.8"
            from(components["java"])
        }
    }
}


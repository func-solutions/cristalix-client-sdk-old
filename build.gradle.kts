plugins {
    kotlin("jvm") version "1.4.21"
}

group = "ru.cristalix"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        setUrl("https://repo.implario.dev/public")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("dev.xdark", "clientapi", "1.0")
}

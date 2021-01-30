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
    implementation("org.lwjgl.lwjgl", "lwjgl", "2.9.3")
    implementation("org.lwjgl.lwjgl", "lwjgl_util", "2.9.2")
}


tasks {
    jar {

    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.6"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.6"
    }
}
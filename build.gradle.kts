plugins {
    kotlin("jvm") version "1.4.21"
}

group = "ru.cristalix"
version = "1.0-SNAPSHOT"

allprojects {

    apply("plugin" to "kotlin")

    repositories {
        mavenCentral()
        maven {
            setUrl("https://repo.implario.dev/public")
        }
    }

    dependencies {
        implementation(kotlin("stdlib"))
        compileOnly("dev.xdark", "clientapi", "1.0.1")
        compileOnly("org.lwjgl.lwjgl", "lwjgl", "2.9.3")
        compileOnly("org.lwjgl.lwjgl", "lwjgl_util", "2.9.2")
    }

    tasks {
        jar {
            from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        }
        compileKotlin {
            kotlinOptions.jvmTarget = "1.6"
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = "1.6"
        }
    }


}

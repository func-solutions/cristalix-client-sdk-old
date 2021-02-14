plugins {
    kotlin("jvm") version "1.4.21"
}

group = "ru.cristalix"
version = "3.0.1"

allprojects {

    apply("plugin" to "kotlin")

    repositories {
        mavenCentral()
        maven {
            setUrl("https://repo.implario.dev/public")
        }
    }

    dependencies {
        compileOnly(kotlin("stdlib"))
        compileOnly("dev.xdark", "clientapi", "1.0.1")
        compileOnly("org.lwjgl.lwjgl", "lwjgl", "2.9.3")
        compileOnly("org.lwjgl.lwjgl", "lwjgl_util", "2.9.2")
    }

    tasks {
        jar {
            from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
            duplicatesStrategy = DuplicatesStrategy.FAIL
        }
        compileKotlin {
            kotlinOptions.jvmTarget = "1.6"
            kotlinOptions.noStdlib = false
            kotlinOptions.noReflect = true
            kotlinOptions.freeCompilerArgs += "-Xno-param-assertions"
            kotlinOptions.freeCompilerArgs += "-Xno-call-assertions"
            kotlinOptions.freeCompilerArgs += "-Xno-receiver-assertions"
            kotlinOptions.freeCompilerArgs += "-Xassertions=always-disable"
//            kotlinOptions.noJdk = true
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = "1.6"
            kotlinOptions.noStdlib = false
            kotlinOptions.noReflect = true
            kotlinOptions.freeCompilerArgs += "-Xno-param-assertions"
            kotlinOptions.freeCompilerArgs += "-Xno-call-assertions"
//            kotlinOptions.noJdk = true
        }
    }


}

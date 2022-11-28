plugins {
    kotlin("jvm") version "1.7.10"
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
tasks.compileKotlin.get().kotlinOptions.run {
    jvmTarget = "1.8"
    freeCompilerArgs += listOf("-opt-in=kotlin.contracts.ExperimentalContracts")
}

publishing {
    publications {
        create<MavenPublication>("uiengine") {
            groupId = "ru.cristalix"
            artifactId = "uiengine"
            version = "anelfer-edition-1.0.3"
            from(components["java"])
        }
    }
}


plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.14.0"
}

val artifact = "bundler"
version = "1.0.0"
group = "dev.implario"

gradlePlugin {
    plugins {
        create("bundlerPlugin") {
            id = "dev.implario.bundler"
            displayName = "Cristalix bundler"
            description = "Plugin for creating binaries compatible with the cristalix client"
            implementationClass = "ru.cristalix.bundler.BundlerPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/cristalix-arcades/cristalix-client-sdk/"
    vcsUrl = "https://github.com/cristalix-arcades/cristalix-client-sdk/"
    tags = listOf("minecraft", "cristalix", "bundler")
}

dependencies {

    implementation("com.android.tools.build:gradle:2.3.0")
    api("net.sf.proguard:proguard-gradle:6.2.2")

    compileOnly(gradleApi())

    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")

}


publishing {
    publications {
        create<MavenPublication>("bundler") {
            groupId = "${project.group}"
            artifactId = artifact
            version = "${project.version}"
            from(components["java"])
        }
    }
}


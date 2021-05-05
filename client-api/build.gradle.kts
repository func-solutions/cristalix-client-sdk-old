plugins {
    `java-library`
}

group = "ru.cristalix"

dependencies {
//    api("org.lwjgl.lwjgl", "lwjgl", "2.9.3")
    api("org.lwjgl.lwjgl", "lwjgl_util", "2.9.3")
    api("io.netty", "netty-buffer", "4.1.58.Final")
    api("com.mojang", "authlib", "2.1.28")
    api("dev.xdark", "clientapi", "live-SNAPSHOT")
    api("dev.xdark", "feder", "1.0-SNAPSHOT")
    api("com.google.guava", "guava", "21.0")
    api("com.google.code.gson", "gson", "2.8.6")
}

publishing {
    publications {
        create<MavenPublication>("clientApi") {
            groupId = "ru.cristalix"
            artifactId = "client-api"
            version = "latest-SNAPSHOT"
            from(components["java"])
        }
    }
}

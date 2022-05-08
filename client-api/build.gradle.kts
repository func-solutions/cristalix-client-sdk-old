plugins {
    `java-library`
}

group = "ru.cristalix"

dependencies {
    api("org.lwjgl.lwjgl", "lwjgl_util", "2.9.3")
    api("io.netty", "netty-buffer", "4.1.58.Final")
    api("com.mojang", "authlib", "2.1.28")
    api("dev.xdark", "feder", "live-SNAPSHOT")
    api("dev.xdark", "clientapi", "1.0.0-SNAPSHOT")
    api("com.google.guava", "guava", "21.0")
    api("com.google.code.gson", "gson", "2.8.6")
    api("it.unimi.dsi", "fastutil", "8.3.1")
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

publishing {
    publications {
        create<MavenPublication>("clientApi") {
            groupId = "ru.cristalix"
            artifactId = "client-api"
            version = "1.0.0"
            from(components["java"])
        }
    }
}

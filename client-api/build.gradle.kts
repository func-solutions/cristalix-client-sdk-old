dependencies {
    compileOnlyApi("org.lwjgl.lwjgl:lwjgl_util:2.9.3")
    compileOnlyApi("io.netty:netty-buffer:4.1.58.Final")
    compileOnlyApi("com.mojang:authlib:2.1.28")
    compileOnlyApi("dev.xdark:feder:live-SNAPSHOT")
    compileOnlyApi("dev.xdark:clientapi:1.0.0-SNAPSHOT")
    compileOnlyApi("com.google.guava:guava:21.0")
    compileOnlyApi("com.google.code.gson:gson:2.8.6")
    compileOnlyApi("it.unimi.dsi:fastutil:8.3.1")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "client-api"

            from(components["java"])
        }
    }
}

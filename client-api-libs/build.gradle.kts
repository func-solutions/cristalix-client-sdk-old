
apply("plugin" to "java")

repositories {
    maven {
        setUrl("https://libraries.minecraft.net/")
    }
}

dependencies {
    implementation("org.lwjgl.lwjgl", "lwjgl", "2.9.3")
    implementation("org.lwjgl.lwjgl", "lwjgl_util", "2.9.3")
    implementation("com.mojang", "authlib", "1.5.25")
    implementation("io.netty", "netty-buffer", "4.1.58.Final")
}


tasks {
    jar {
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

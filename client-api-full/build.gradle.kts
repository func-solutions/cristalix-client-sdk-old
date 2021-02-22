
apply("plugin" to "java")

repositories {
    maven {
        setUrl("https://libraries.minecraft.net/")
    }
}

dependencies {
    implementation("dev.xdark", "clientapi", "1.0")
    implementation("org.lwjgl.lwjgl", "lwjgl", "2.9.3")
    implementation("org.lwjgl.lwjgl", "lwjgl_util", "2.9.3")
    implementation("com.mojang", "authlib", "1.5.25")
}


tasks {
    jar {
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

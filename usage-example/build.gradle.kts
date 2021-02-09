buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.3.0")
        classpath("com.guardsquare:proguard-gradle:7.0.0")
    }
}

dependencies {
    implementation(project(":engine"))
}


val proguard = task("jarFlattened", proguard.gradle.ProGuardTask::class) {
    dependsOn(tasks.jar)
    val inJarFile: File = tasks.jar.get().archiveFile.get().asFile

    val outJarFile = File(inJarFile.parentFile, inJarFile.name.replace(".jar", "-compatible.jar"))

    injars(inJarFile)
    outjars(outJarFile)

    this.extensions.add("file", outJarFile)

    libraryjars(configurations.compileOnly.get())
    configurations.compileOnly.get().forEach(::println)

    configuration(File(rootDir, "proguard.pro"))

    this.group = "build"
}

task("jarPure", Jar::class) {
    dependsOn(proguard)
    from(zipTree(proguard.extensions.getByName("file")).matching {
        exclude {
            val path = it.relativePath.pathString
//            return@exclude path.startsWith("kotlin/") || path.startsWith("META-INF/")
            return@exclude path.endsWith(".kotlin_metadata") ||
                    path.endsWith(".kotlin_builtins") ||
                    path.startsWith("META-INF/")
        }
    })
    this.archiveAppendix.set("pure")
    this.group = "build"
    this.manifest

}

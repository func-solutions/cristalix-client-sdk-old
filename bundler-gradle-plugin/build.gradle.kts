plugins {
    `java-gradle-plugin`
}

repositories {
    google()
    jcenter()
}

version = "2.0.2"

gradlePlugin {
    plugins {
        this.create("bundlerPlugin") {
            id = "ru.cristalix.uiengine.bundler"
            implementationClass = "ru.cristalix.uiengine.bundler.BundlerPlugin"
        }
    }
}

dependencies {
    compileOnly(gradleApi())
    compileOnly("com.android.tools.build:gradle:3.3.0")
    compileOnly("com.guardsquare:proguard-gradle:7.0.0")
}

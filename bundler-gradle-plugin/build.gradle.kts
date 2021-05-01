plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.12.0"
}

repositories {
    google()
    jcenter()
}

pluginBundle {

}

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
    implementation("com.android.tools.build:gradle:3.3.0")
    implementation("com.guardsquare:proguard-gradle:7.0.0")
}


publishing {
    publications {
        create<MavenPublication>("bundler") {
            groupId = "ru.cristalix"
            artifactId = "bundler"
            version = "2.3.8-rc1"
            from(components["java"])
        }
    }
}


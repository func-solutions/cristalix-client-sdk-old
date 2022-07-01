dependencies {
    api(projects.clientSdk)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "uiengine"

            from(components["java"])
        }
    }
}

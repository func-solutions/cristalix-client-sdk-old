dependencies {
    compileOnlyApi(projects.clientApi)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "client-sdk"

            from(components["java"])
        }
    }
}

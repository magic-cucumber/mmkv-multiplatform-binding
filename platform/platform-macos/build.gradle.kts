import com.vanniktech.maven.publish.KotlinJvm

import java.io.FileInputStream
import java.security.MessageDigest

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.vanniktech.maven.publish")
}

group = "top.kagg886.mkmb"
version()

kotlin {
    jvmToolchain(22)
}

val processBuild = tasks.register<Exec>("processBuild") {
    onlyIf {
        System.getProperty("os.name").startsWith("Mac")
    }
    workingDir = project.file("native-binding-macos")
    commandLine(
        "zsh", "-c",
        """
            mkdir -p build && \
            cd build && \
            cmake .. && \
            make
        """.trimIndent()
    )
}

// Configure JVM processResources task
tasks.named<ProcessResources>("processResources") {
    dependsOn(processBuild)
    from(project.file("native-binding-macos/build/libmmkvc.dylib"))
}


publishing(KotlinJvm())

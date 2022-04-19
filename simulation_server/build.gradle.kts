val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.20"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "projectcows"
version = "0.0.1"
application {
    mainClass.set("projectcows.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.mini2Dx:gdx-math:1.9.13")
    implementation("com.google.code.gson:gson:2.9.0")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "io.ktor.server.netty.EngineMain"))
        }
    }
}
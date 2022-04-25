import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val gdx_version: String by project
plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    kotlin("plugin.serialization") version "1.4.32"
    application
}

group = "com.cows"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}


dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-websockets-jvm:$ktor_version")
    implementation("org.mini2Dx:gdx-math:1.9.13")
    api("com.badlogicgames.gdx:gdx:$gdx_version")
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}
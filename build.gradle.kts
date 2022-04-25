val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val gdx_version: String by project

buildscript {

    val kot_ver = "1.6.10"

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/releases/") }
        maven { url = uri("https://jitpack.io") }
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("com.mobidevelop.robovm:robovm-gradle-plugin:2.3.15")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kot_ver")
    }
}

/*dependencies {
    implementation("com.android.tools.build:gradle:7.0.4")
    implementation("com.mobidevelop.robovm:robovm-gradle-plugin:2.3.16")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
}*/

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}


group = "projectcows"
version = "1.0"

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/releases/") }
    maven { url = uri("https://jitpack.io") }
    google()
}


project(":client") {
    apply(plugin = "kotlin")
    evaluationDependsOn(":shared")

    kotlin{
        sourceSets["main"].apply {
            kotlin.srcDir("../shared/src/main/kotlin/")
        }
    }
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/releases/") }
        maven { url = uri("https://jitpack.io") }
        google()
    }
}



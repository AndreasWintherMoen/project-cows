package com.cows.game

import kotlin.jvm.JvmStatic
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration()
        config.setWindowedMode(Application.WIDTH.toInt(), Application.HEIGHT.toInt())
        config.setForegroundFPS(60)
        Lwjgl3Application(Application(), config)
    }
}
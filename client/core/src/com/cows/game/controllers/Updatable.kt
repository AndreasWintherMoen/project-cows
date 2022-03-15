package com.cows.game.controllers

import com.cows.game.Updater
import com.cows.game.views.Renderable

abstract class Updatable {
    abstract fun update(deltaTime:Float)
    abstract val renderableView: Renderable

    init{
        Updater.addUpdatable(this)
    }
}
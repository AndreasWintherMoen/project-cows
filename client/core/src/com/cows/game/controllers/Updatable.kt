package com.cows.game.controllers

import com.cows.game.Updater
import com.cows.game.views.Renderable

abstract class Updatable {
    abstract fun update(deltaTime:Float)

    open fun die() {
        Updater.removeUpdatable(this)
    }

    init{
        Updater.addUpdatable(this)
    }
}
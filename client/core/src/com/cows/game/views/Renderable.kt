package com.cows.game.views

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cows.game.managers.Renderer

abstract class Renderable {
    abstract fun render(batch: SpriteBatch, deltaTime: Float)
    abstract fun dispose()
    var hide: Boolean = false

    open fun die() {
        Renderer.removeRenderable(this)
    }

    init {
        Renderer.addRenderable(this)
    }
}
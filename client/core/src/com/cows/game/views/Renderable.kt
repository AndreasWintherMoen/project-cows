package com.cows.game.views

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cows.game.Renderer

abstract class Renderable {
    abstract fun render(batch: SpriteBatch, deltaTime: Float)
    abstract fun dispose()

    init {
        Renderer.instance.addRenderable(this)
    }
}
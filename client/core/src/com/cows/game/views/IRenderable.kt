package com.cows.game.views

import com.badlogic.gdx.graphics.g2d.SpriteBatch

interface IRenderable {
    fun render(batch: SpriteBatch, deltaTime: Float)
    fun dispose()
}
package com.cows.game.hud

import com.badlogic.gdx.graphics.g2d.Sprite

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.cows.game.ClickSubscriber
import com.cows.game.controllers.TileController
import com.cows.game.views.Renderable

class SmartObject(textureFilePath: String, position: Vector2, var scale: Float): Renderable() {


    var sprite = Sprite(Texture(textureFilePath))

    init {
        sprite.setPosition(position.x, position.y)
        sprite.setScale(scale)
    }

    fun setPosition(newPosition: Vector2) = sprite.setPosition(newPosition.x, newPosition.y)

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        sprite.draw(batch)
    }

    override fun dispose() {
        sprite.texture.dispose()
    }

    fun setTextureFilePath(newFilePath: String) {
        sprite.texture = Texture(newFilePath)
    }

}
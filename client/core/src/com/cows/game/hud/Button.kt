package com.cows.game.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.cows.game.views.Renderable


class Button(textureFilePath: String, var position: Vector2, var onClick: () -> Unit): Renderable() {
    constructor(textureFilePath: String) : this(textureFilePath, Vector2(), {})
    constructor(textureFilePath: String, position: Vector2) : this(textureFilePath, position, {})
    constructor(textureFilePath: String, onClick: () -> Unit) : this(textureFilePath, Vector2(), onClick)

    var texture = Texture(textureFilePath)

    private fun isWithinBounds(pos: Vector2): Boolean {
        if (pos.x < position.x) return false
        if (pos.y < position.y) return false
        if (pos.x > position.x + texture.width) return false
        if (pos.y > position.y + texture.height) return false
        return true
    }

    private fun isClicked(): Boolean {
        if (!Gdx.input.justTouched()) return false

        val clickedPosition = Vector2(Gdx.input.x.toFloat(), Gdx.graphics.height - Gdx.input.y.toFloat())

        return isWithinBounds(clickedPosition)
    }

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        batch.draw(texture, position.x, position.y)

        if (isClicked()) onClick.invoke()
    }

    override fun dispose() {
        texture.dispose()
    }
}
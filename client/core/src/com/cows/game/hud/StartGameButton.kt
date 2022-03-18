package com.cows.game.hud

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.cows.game.views.Renderable


class StartGameButton: Renderable() {
    private val texture = Texture("HUD/start-button.png")
    private val position = Vector2(0f, 0f)

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        batch.draw(texture, position.x, position.y)
    }

    override fun dispose() {
        texture.dispose()
    }
}
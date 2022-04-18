package com.cows.game.hud

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.cows.game.Application
import com.cows.game.views.Renderable


abstract class ActionPanel(): Renderable() {
    private val texture = Texture("HUD/panel.jpg")
    private val position = Vector2(Application.WIDTH - WIDTH, 0f)

    companion object {
        const val WIDTH = 200f
    }

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        batch.draw(texture, position.x, position.y)
    }
}
package com.cows.game.hud

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.cows.game.Application
import com.cows.game.views.Renderable


abstract class ActionPanel(): Renderable() {
    private val texture = Texture("HUD/panel.jpg")
    protected val position = Vector2(Application.WIDTH - WIDTH, 0f)
    companion object {
        const val WIDTH = 200f
        const val PADDING = 10f
        const val HEADER_HEIGHT = 50f
        const val ACTION_HEIGHT = Application.HEIGHT - HEADER_HEIGHT - PADDING
        const val UNIT_MARGIN = 12f
    }


    init {
        zIndex = 3
    }

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        batch.draw(texture, position.x, position.y)
    }

    override fun dispose() {
        texture.dispose()
    }
}
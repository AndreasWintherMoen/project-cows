package com.cows.game.hud

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.cows.game.views.Renderable


class StartGameButton(onClick: () -> Unit): Button(onClick) {
    override val texture = Texture("HUD/start-button.png")
    override var position = Vector2(0f, 0f)
}
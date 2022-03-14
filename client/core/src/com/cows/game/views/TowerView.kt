package com.cows.game.views

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cows.game.models.Tower

class TowerView(val model: Tower): Renderable() {
    val texture = Texture("tower.png")

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        val pixel = model.position.toPixel()
        batch.draw(texture, pixel.x, pixel.y)
    }

    override fun dispose() {
        texture.dispose()
    }

}
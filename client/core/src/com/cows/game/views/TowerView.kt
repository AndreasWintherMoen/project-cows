package com.cows.game.views

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cows.game.models.TowerModel

class TowerView(val model: TowerModel): Renderable() {
    val texture = Texture("tower.png")

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        val pixel = model.tileCoordinate.toVector2()
        batch.draw(texture, pixel.x, pixel.y)
    }

    override fun dispose() {
        texture.dispose()
    }

}
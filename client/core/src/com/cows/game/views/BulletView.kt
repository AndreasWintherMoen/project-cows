package com.cows.game.views

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cows.game.models.BulletModel

class BulletView (val model: BulletModel ) : Renderable() {
    companion object {
        fun modelToSprite(): Sprite = Sprite(Texture("Bullets/blue.png", ))
    }

    private var sprite = modelToSprite()

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        if (model.hasHit) return
        sprite.setPosition(model.position.x, model.position.y)
        sprite.draw(batch)
    }

    override fun dispose() {
        sprite.texture.dispose()
    }

}
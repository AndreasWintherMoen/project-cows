package com.cows.game.views

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cows.game.map.Coordinate
import com.cows.game.enums.UnitType
import com.cows.game.models.UnitModel

class UnitView (val model: UnitModel): Renderable() {
    companion object {
        private fun directionToFileName(direction: Coordinate): String {
            if (direction.x < 0) return "left"
            if (direction.x > 0) return "right"
            if (direction.y < 0) return "front"
            if (direction.y > 0) return "back"
            throw Error("Could not find direction $direction")
        }
        fun modelToTexture(model: UnitModel, walkState: Int): Texture = Texture("Units/Units/${model.type.toString().lowercase()}-${model.level.toString()}-${directionToFileName(model.currentDirection)}-${walkState.toString()}.png")
        fun modelToSprite(model: UnitModel, walkState: Int): Sprite = Sprite(modelToTexture(model, walkState))
    }

    private var walkState = 0;
    private var stepsPerSecond = 0.2f * model.movementSpeed;
    private var elapsedTime = 0f;
    private var sprite = modelToSprite(model, walkState)

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        if (model.isDead) return

        elapsedTime += deltaTime
        if (elapsedTime >= 1/stepsPerSecond){
            elapsedTime = 0f
            walkState = if(walkState == 1) 0 else 1
//            sprite.texture = Texture("Units/${unitTypeToFolder(model.type)}/${unitTypeToFolder(model.type)}-${directionToFileName(model.currentDirection)}${walkState}.png")
            sprite.texture = modelToTexture(model, walkState)
        }

        sprite.setPosition(model.position.x, model.position.y)
        sprite.draw(batch)
    }

    override fun dispose() {
      sprite.texture.dispose()
    }

    fun updateSprite() {
        sprite = modelToSprite(model, walkState)
    }
}
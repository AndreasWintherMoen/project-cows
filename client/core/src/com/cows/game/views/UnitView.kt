package com.cows.game.views

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cows.game.map.Coordinate
import com.cows.game.enums.UnitType
import com.cows.game.models.UnitModel

class UnitView (val model: UnitModel): Renderable() {
    companion object {
        fun unitTypeToFolder(unitType: UnitType): String =
            when(unitType) {
                UnitType.NONE -> "NONE"
                UnitType.FIRE -> "charmander"
                UnitType.WATER -> "squirtle"
                UnitType.GRASS -> "bulbasaur"
                else -> throw Error("Could not find unit type $unitType")
            }
        fun directionToFileName(direction: Coordinate): String {
            if (direction.x < 0) return "left-0"
            if (direction.x > 0) return "right-0"
            if (direction.y < 0) return "front-0"
            if (direction.y > 0) return "back-0"
            throw Error("Could not find direction $direction")
        }
        fun modelToSprite(model: UnitModel): Sprite = Sprite(Texture("Units/${unitTypeToFolder(model.type)}/${unitTypeToFolder(model.type)}-${directionToFileName(model.currentDirection)}.png"))
    }

    private var sprite = modelToSprite(model)

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        if (model.isDead) return
        sprite.setPosition(model.position.x, model.position.y)
        sprite.draw(batch)
    }

    override fun dispose() {
      sprite.texture.dispose()
    }

    fun updateSprite() {
        sprite = modelToSprite(model)
    }
}
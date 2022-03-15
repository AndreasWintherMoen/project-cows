package com.cows.game.views

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cows.game.Coordinate
import com.cows.game.enums.TileType
import com.cows.game.enums.UnitType
import com.cows.game.models.TileModel
import com.cows.game.models.UnitModel

class UnitView (val model: UnitModel): Renderable() {
    companion object {
        fun unitTypeToSprite(unitType: UnitType): Sprite =
            when(unitType) {
                UnitType.INDIAN_UNIT -> Sprite(Texture("IndianUnit.png"))
                UnitType.SWORDMAN -> Sprite(Texture("IndianUnit.png"))
                UnitType.RUNNER -> Sprite(Texture("tower.png"))
                UnitType.TANK -> Sprite(Texture("IndianUnit.png"))
                else -> throw Error("Could not find unit type $unitType")
            }
    }

    private val sprite = unitTypeToSprite(model.type)

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        sprite.setPosition(model.position.x, model.position.y)
        sprite.draw(batch)
    }

    override fun dispose() {
      sprite.texture.dispose()
    }
}
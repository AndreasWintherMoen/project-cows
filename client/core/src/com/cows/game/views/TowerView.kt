package com.cows.game.views

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cows.game.enums.UnitType
import com.cows.game.models.TileModel
import com.cows.game.models.TowerModel

class TowerView(private val model: TowerModel): Renderable() {
    private val pokemonTower = Sprite(typeToTowerTexture(model.type))

    companion object {
        fun typeToTowerTexture(unitType: UnitType): Texture =
            when(unitType) {
                UnitType.FIRE -> Texture("Towers/fire-2-back-0.png")
                UnitType.GRASS -> Texture("Towers/grass-2-back-0.png")
                UnitType.WATER -> Texture("Towers/water-2-back-0.png")
                else -> throw Error("Could not find tile type $unitType")
            }
    }

    init {
        val pixel = model.tileCoordinate.toVector2()
        pokemonTower.setPosition(pixel.x, pixel.y+(TileModel.HEIGHT - (TileModel.WIDTH/pokemonTower.width)*pokemonTower.height)/2)
        pokemonTower.setSize(TileModel.WIDTH, (TileModel.WIDTH/pokemonTower.width)*pokemonTower.height)
        pokemonTower.setOrigin(pokemonTower.width/2f, pokemonTower.height/2f)
    }

    private fun rotateTowardTarget(deltaTime: Float){
        if (!model.hasTarget) {
            return
        }
        pokemonTower.rotation = model.rotation
    }

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        rotateTowardTarget(deltaTime)
        pokemonTower.draw(batch)
    }

    override fun dispose() {
        pokemonTower.texture.dispose()
    }
}
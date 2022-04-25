package com.cows.game.views

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cows.game.enums.UnitType
import com.cows.game.models.TileModel
import com.cows.game.models.TowerModel

class TowerView(private val model: TowerModel): Renderable() {
    private val pokemonTower = Sprite(modelToTowerTexture(model))

    companion object {
        fun modelToTowerTexture(model: TowerModel): Texture =
            when (model.type) {
                UnitType.FIRE ->
                    when (model.level) {
                        0 -> Texture("Units/Units/fire-0-back-0.png")
                        1 -> Texture("Units/Units/fire-1-back-0.png")
                        2 -> Texture("Towers/fire-2-back-0.png")
                        else -> throw Error("Could not find level ${model.level}")
                    }

                UnitType.GRASS ->
                    when (model.level) {
                        0 -> Texture("Units/Units/grass-0-back-0.png")
                        1 -> Texture("Units/Units/grass-1-back-0.png")
                        2 -> Texture("Towers/grass-2-back-0.png")
                        else -> throw Error("Could not find level ${model.level}")
                    }
                UnitType.WATER ->
                    when (model.level) {
                        0 -> Texture("Units/Units/water-0-back-0.png")
                        1 -> Texture("Units/Units/water-1-back-0.png")
                        2 -> Texture("Towers/water-2-back-0.png")
                        else -> throw Error("Could not find level ${model.level}")
                    }
                else -> throw Error("Could not find unit type ${model.type}")
            }
    }

    init {
        val pixel = model.tileCoordinate.toVector2()
        pokemonTower.setPosition(pixel.x, pixel.y+(TileModel.HEIGHT - (TileModel.WIDTH/pokemonTower.width)*pokemonTower.height)/2)
        pokemonTower.setSize(TileModel.WIDTH, (TileModel.WIDTH/pokemonTower.width)*pokemonTower.height)
        pokemonTower.setOrigin(pokemonTower.width/2f, pokemonTower.height/2f)
    }

    private fun rotateTowardTarget(){
        if (!model.hasTarget) {
            return
        }
        pokemonTower.rotation = model.rotation
    }

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        rotateTowardTarget()
        pokemonTower.draw(batch)
    }

    override fun dispose() {
        pokemonTower.texture.dispose()
    }
}
package com.cows.game.views

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cows.game.models.TileModel
import com.cows.game.models.TowerModel

class TowerView(private val model: TowerModel): Renderable() {
    private val tower = Sprite(Texture("Towers/tower1.png"))
    private val turret = Sprite(Texture("Turrets/turret1.png"))

    init {
        val pixel = model.tileCoordinate.toVector2()
        tower.setPosition(pixel.x+ TileModel.WIDTH*0.15f, pixel.y+(TileModel.WIDTH/tower.width)*tower.height*0.15f)
        tower.setSize(TileModel.WIDTH*0.7f, (TileModel.WIDTH/tower.width)*tower.height*0.7f)
        turret.setPosition(pixel.x, pixel.y+(TileModel.HEIGHT - (TileModel.WIDTH/turret.width)*turret.height)/2)
        turret.setSize(TileModel.WIDTH, (TileModel.WIDTH/turret.width)*turret.height)
        turret.setOrigin(turret.width/2f, turret.height/2f)
    }

    private fun rotateTowardTarget(deltaTime: Float){
        if (!model.hasTarget) {
            turret.rotate(100*deltaTime)
            return
        }
        turret.rotation = model.rotation
    }

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        rotateTowardTarget(deltaTime)
        tower.draw(batch)
        turret.draw(batch)
    }

    override fun dispose() {
        tower.texture.dispose()
        turret.texture.dispose()
    }
}
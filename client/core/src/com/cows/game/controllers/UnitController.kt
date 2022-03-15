package com.cows.game.controllers

import com.badlogic.gdx.math.Vector2
import com.cows.game.models.UnitModel
import com.cows.game.views.TowerView
import com.cows.game.views.UnitView


class UnitController(private val model: UnitModel): Updatable() {
    private val view = UnitView(model)
    private var target = Vector2();
    override val renderableView = UnitView(model)

    override fun update(deltaTime: Float) {
        TODO("Not yet implemented")
    }

    fun move(tile: TileController) {
        target = tile.tileModel.coordinate.toVector2()
    }

    fun die() {
        model.isDead = true;
    }

    fun win() {
        // TODO: Implement this
    }

    fun spawn(tile: TileController) {
        model.position = tile.tileModel.coordinate.toVector2()
    }

}
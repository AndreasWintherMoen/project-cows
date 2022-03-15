package com.cows.game.controllers

import com.cows.game.models.UnitModel
import com.cows.game.views.TowerView
import com.cows.game.views.UnitView

class UnitController(private val model: UnitModel): Updatable() {
    override fun update(deltaTime: Float) {
        TODO("Not yet implemented")
    }

    override val renderableView = UnitView(model)

    fun move(tile: TileController) {
        // TODO: Implement this
    }

    fun die() {
        // TODO: Implement this
    }

    fun win() {
        // TODO: Implement this
    }

    fun spawn(tile: TileController) {
        // TODO: Implement this
    }
}
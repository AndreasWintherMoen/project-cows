package com.cows.game.controllers

import com.cows.game.models.UnitModel
import com.cows.game.views.UnitView

class UnitController(private val model: UnitModel) {
    private val view = UnitView(model)

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
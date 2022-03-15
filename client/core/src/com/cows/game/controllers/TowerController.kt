package com.cows.game.controllers

import com.cows.game.models.TowerModel
import com.cows.game.views.TowerView

class TowerController(val model: TowerModel) {
    val view = TowerView(model)

    fun target(unit: UnitController) {
        // TODO: Implement this
    }

    fun attack() {
        // TODO: Implement this
    }
}
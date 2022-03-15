package com.cows.game.controllers

import com.cows.game.models.TowerModel
import com.cows.game.views.Renderable
import com.cows.game.views.TowerView

class TowerController(val model: TowerModel): Updatable() {
    override fun update(deltaTime: Float) {
       // TODO("Not yet implemented")
    }

    override val renderableView = TowerView(model)

    fun target(unit: UnitController) {
        // TODO: Implement this
    }

    fun attack() {
        // TODO: Implement this
    }
}
package com.cows.game.controllers

import com.cows.game.models.TowerModel
import com.cows.game.views.TowerView

class PlanningTowerController(val model: TowerModel) {
    val view = TowerView(model)
}
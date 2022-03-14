package com.cows.game.controllers

import com.cows.game.models.Tower
import com.cows.game.views.TowerView

class TowerController(val model: Tower) {
    val view = TowerView(model)
}
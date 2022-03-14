package com.cows.game.controllers

import com.cows.game.models.Unit
import com.cows.game.views.UnitView

class UnitController(private val model: Unit) {
    private val view = UnitView(model)
}
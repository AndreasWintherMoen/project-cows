package com.cows.game.roundSimulation

import com.cows.game.models.Tower

data class RoundSimulation(
    val towerList: List<Tower>?,
    val unitList: List<Unit>?,
    val eventLog: List<Action>?
)

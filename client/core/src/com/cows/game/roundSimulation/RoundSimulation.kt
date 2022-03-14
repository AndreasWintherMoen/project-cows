package com.cows.game.roundSimulation

import com.cows.game.models.Tower
import com.cows.game.models.Unit

data class RoundSimulation(
    val towerList: List<Tower>,
    val unitList: List<Unit>,
    val eventLog: List<Tick>
)

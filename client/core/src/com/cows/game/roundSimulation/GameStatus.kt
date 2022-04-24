package com.cows.game.roundSimulation

import com.cows.game.roundSimulation.rawJsonData.JsonAvailableTowers
import com.cows.game.roundSimulation.rawJsonData.JsonAvailableUnits
import com.cows.game.roundSimulation.rawJsonData.JsonTower
import com.cows.game.roundSimulation.rawJsonData.JsonUnit

data class GameStatus(
    val playerStates: Pair<PlayerState, PlayerState>,
    var path: List<IntArray>,
    var attackInstructions: List<JsonUnit>,
    var defendInstructions: List<JsonTower>,
    var availableUnits: JsonAvailableUnits,
    var availableTowers: JsonAvailableTowers,
    var roundCounter: Int
)

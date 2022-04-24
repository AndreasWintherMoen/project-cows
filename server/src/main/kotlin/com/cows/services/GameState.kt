package com.cows.services

import com.cows.services.simulation.models.json.JsonAvailableTowers
import com.cows.services.simulation.models.json.JsonAvailableUnits
import projectcows.rawJsonData.JsonTower
import projectcows.rawJsonData.JsonUnit

data class GameState (
    val playerStates: Pair<PlayerState, PlayerState>,
    var path: List<IntArray>) {
    var attackInstructions: List<JsonUnit>? = null
    var defendInstructions: List<JsonTower>? = null
    var availableUnits: JsonAvailableUnits? = null
    var availableTowers: JsonAvailableTowers? = null
    var roundCounter: Int = 0
}
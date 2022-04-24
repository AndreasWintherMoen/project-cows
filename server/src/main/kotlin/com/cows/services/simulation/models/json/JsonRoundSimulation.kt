package com.cows.services.simulation.models.json

import projectcows.rawJsonData.JsonTick
import projectcows.rawJsonData.JsonTower
import projectcows.rawJsonData.JsonUnit

data class JsonRoundSimulation (
    val towerList: List<JsonTower>,
    val unitList: List<JsonUnit>,
    val eventLog: List<JsonTick>,
    val attackerWon: Boolean
)


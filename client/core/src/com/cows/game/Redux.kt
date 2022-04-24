package com.cows.game

import com.cows.game.map.Coordinate
import com.cows.game.roundSimulation.rawJsonData.JsonAvailableTowers
import com.cows.game.roundSimulation.rawJsonData.JsonAvailableUnits
import com.cows.game.roundSimulation.rawJsonData.JsonRoundSimulation

object Redux {
    fun init() {}

    var jsonRoundSimulation: JsonRoundSimulation? = null
    var jsonAvailableUnits: JsonAvailableUnits? = null
    var jsonAvailableTowers: JsonAvailableTowers? = null
    var path: ArrayList<Coordinate>? = null

}
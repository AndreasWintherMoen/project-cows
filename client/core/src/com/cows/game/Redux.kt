package com.cows.game

import com.cows.game.roundSimulation.GameStatus
import com.cows.game.roundSimulation.rawJsonData.JsonRoundSimulation

object Redux {
    fun init() {}

    var jsonRoundSimulation: JsonRoundSimulation? = null
    var gameStatus: GameStatus? = null
    var playerCreatedGame: Boolean? = null
    var errorMessage: String? = null
}
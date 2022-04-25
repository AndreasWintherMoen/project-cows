package com.cows.game.managers

import com.cows.game.Redux
import com.cows.game.enums.GameState
import com.cows.game.gameState.GameStateSubscriber
import com.cows.game.roundSimulation.GameStatus
import com.cows.game.roundSimulation.rawJsonData.JsonRoundSimulation

object RoundManager: GameStateSubscriber() {
    var playerIsAttacker = false
    var roundSimulation: JsonRoundSimulation? = null
    var gameStatus: GameStatus? = null

    fun init() {}

    override fun onChangeGameState(oldGameState: GameState, newGameState: GameState) {
        if (newGameState == GameState.PLANNING_ATTACK) playerIsAttacker = true
        else if (newGameState == GameState.PLANNING_DEFENSE) playerIsAttacker = false
    }

    fun reloadReduxValues() {
        roundSimulation = Redux.jsonRoundSimulation
        gameStatus = Redux.gameStatus
    }
}
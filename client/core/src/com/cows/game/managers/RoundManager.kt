package com.cows.game.managers

import com.cows.game.enums.GameState
import com.cows.game.gameState.GameStateSubscriber

object RoundManager: GameStateSubscriber() {
    var playerIsAttacker = false

    fun init() {}

    override fun onChangeGameState(oldGameState: GameState, newGameState: GameState) {
        if (newGameState == GameState.PLANNING_ATTACK) playerIsAttacker = true
        else if (newGameState == GameState.PLANNING_DEFENSE) playerIsAttacker = false
    }
}
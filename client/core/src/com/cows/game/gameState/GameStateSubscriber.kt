package com.cows.game.gameState

import com.cows.game.enums.GameState
import com.cows.game.managers.GameStateManager

abstract class GameStateSubscriber() {
    abstract fun onChangeGameState(oldGameState: GameState, newGameState: GameState)

    init {
        GameStateManager.addSubscriber(this)
    }
}
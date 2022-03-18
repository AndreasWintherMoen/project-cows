package com.cows.game.hud

import com.cows.game.enums.GameState
import com.cows.game.gameState.GameStateSubscriber
import com.cows.game.managers.GameStateManager


// Should manage which naviagations buttons that should be displayed
class HUDManager: GameStateSubscriber() {
    var buttons = mutableListOf<StartGameButton>()

    override fun onChangeGameState(oldGameState: GameState, newGameState: GameState) {
        when (newGameState) {
            GameState.ACTIVE_GAME -> print("RETURN BUTTONS FOR: ACTIVE GAME")
            GameState.NONE -> print("RETURN BUTTONS FOR: ACTIVE GAME")
            GameState.START_MENU -> print("RETURN BUTTONS FOR: ACTIVE GAME")
            GameState.PLANNING_DEFENSE -> print("RETURN BUTTONS FOR: ACTIVE GAME")
            GameState.PLANNING_ATTACK -> print("x == 1")
        }
    }
}
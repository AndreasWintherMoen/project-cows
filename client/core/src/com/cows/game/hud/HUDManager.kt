package com.cows.game.hud
import com.cows.game.enums.GameState
import com.cows.game.gameState.GameStateSubscriber


// Should manage which naviagations buttons that should be displayed
class HUDManager(private val onStartGame: () -> Unit): GameStateSubscriber() {
    var buttons = mutableListOf<Button>()

    override fun onChangeGameState(oldGameState: GameState, newGameState: GameState) {
        println("Changing game state from $oldGameState to $newGameState")

        buttons.forEach { it.die() }
        buttons.clear()
        when (newGameState) {
            GameState.ACTIVE_GAME -> print("RETURN BUTTONS FOR: ACTIVE GAME")
            GameState.NONE -> print("RETURN BUTTONS FOR: ACTIVE GAME")
            GameState.START_MENU -> print("RETURN BUTTONS FOR: ACTIVE GAME")
            GameState.PLANNING_DEFENSE -> buttons.add(StartGameButton({ onStartGame.invoke() }))
            GameState.PLANNING_ATTACK -> print("x == 1")
        }
    }
}
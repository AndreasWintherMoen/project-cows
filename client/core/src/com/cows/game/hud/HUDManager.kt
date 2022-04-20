package com.cows.game.hud
import com.cows.game.enums.GameState
import com.cows.game.gameState.GameStateSubscriber


// Should manage which naviagations buttons that should be displayed
class HUDManager(private val onStartGame: () -> Unit): GameStateSubscriber() {
    private var actionPanel: ActionPanel? = null


    override fun onChangeGameState(oldGameState: GameState, newGameState: GameState) {
        println("Changing game state from $oldGameState to $newGameState")
        actionPanel?.let {
            it.die()
        }
        when (newGameState) {
            GameState.ACTIVE_GAME -> actionPanel = ActiveGameActionPanel()
            GameState.NONE -> {}
            GameState.START_MENU -> actionPanel = StartMenuActionPanel()
            GameState.PLANNING_DEFENSE -> actionPanel = PlanningDefenseActionPanel {onStartGame.invoke()}
            GameState.PLANNING_ATTACK -> actionPanel = PlanningAttackActionPanel {onStartGame.invoke()}
        }
    }
}
package com.cows.game.hud
import com.cows.game.enums.GameState
import com.cows.game.gameState.GameStateSubscriber
import com.cows.game.map.Map


// Should manage which naviagations buttons that should be displayed
class HUDManager(private val onStartGame: () -> Unit): GameStateSubscriber() {
    private var actionPanel: ActionPanel? = null
    private var startMenu: StartMenu? = null


    override fun onChangeGameState(oldGameState: GameState, newGameState: GameState) {
        println("Changing game state from $oldGameState to $newGameState")
        actionPanel?.let {
            it.die()
        }
        if (oldGameState == GameState.START_MENU) {
            Map.init()
            startMenu = null
        }
        when (newGameState) {
            GameState.ACTIVE_GAME -> actionPanel = ActiveGameActionPanel()
            GameState.NONE -> {}
            GameState.START_MENU -> startMenu = StartMenu()
            GameState.PLANNING_DEFENSE -> actionPanel = PlanningDefenseActionPanel {onStartGame.invoke()}
            GameState.PLANNING_ATTACK -> actionPanel = PlanningAttackActionPanel {onStartGame.invoke()}
        }
    }
}
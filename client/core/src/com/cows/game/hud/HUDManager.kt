package com.cows.game.hud
import com.badlogic.gdx.math.Vector2
import com.cows.game.controllers.MenuController
import com.cows.game.enums.GameState
import com.cows.game.gameState.GameStateSubscriber
import com.cows.game.map.Map


// Should manage which naviagations buttons that should be displayed
class HUDManager(): GameStateSubscriber() {
    private var actionPanel: ActionPanel? = null
    private var menuController: MenuController? = null
    private val winText = SmartObject("HUD/start-button.png", Vector2(200f, 300f), 1f)
    private val loseText = SmartObject("HUD/cancel-button.png", Vector2(200f, 300f), 1f)

    init {
        hideTexts()
    }

    private fun hideTexts() {
        winText.hide = true
        loseText.hide = true
    }

    override fun onChangeGameState(oldGameState: GameState, newGameState: GameState) {
        println("Changing game state from $oldGameState to $newGameState")

        hideTexts()

        actionPanel?.let {
            it.die()
        }
        if (oldGameState == GameState.START_MENU) {
            // Initializing map here in HUDManager seems like a bad choice, but we'll have to change how we load
            // the map, since we're going to get it from the API somehow, so let's just keep it here for now
            menuController?.die()
            menuController = null
        }
        when (newGameState) {
            GameState.ACTIVE_GAME -> actionPanel = ActiveGameActionPanel()
            GameState.NONE -> {}
            GameState.PLANNING_DEFENSE -> actionPanel = PlanningDefenseActionPanel()
            GameState.PLANNING_ATTACK -> actionPanel = PlanningAttackActionPanel()
            GameState.START_MENU -> menuController = MenuController()
        }
    }

    fun showWinText() {
        winText.hide = false
    }

    fun showLoseText() {
        loseText.hide = false
    }
}
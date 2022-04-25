package com.cows.game.hud
import com.badlogic.gdx.math.Vector2
import com.cows.game.Application
import com.cows.game.controllers.MenuController
import com.cows.game.enums.GameState
import com.cows.game.gameState.GameStateSubscriber
import com.cows.game.managers.AudioManager
import com.cows.game.map.Map


// Should manage which naviagations buttons that should be displayed
class HUDManager(): GameStateSubscriber() {
    private var actionPanel: ActionPanel? = null
    private var menuController: MenuController? = null
    private val winText = SmartObject("HUD/win.png", Vector2(200f, 0f), 1f)
    private val loseText = SmartObject("HUD/lose.png", Vector2(200f, 0f), 1f)
    private var healthIndicator: HealthIndicator? = null

    private val muteButton = Button("Buttons/mute-button.png", Vector2(Application.WIDTH - 50f, Application.HEIGHT - 50f)) { onMuteButtonClicked() }
    private val unmuteButton = Button("Buttons/unmute-button.png", Vector2(Application.WIDTH - 50f, Application.HEIGHT - 50f)) { onUnmuteButtonClicked() }

    init {
        hideTexts()
        winText.zIndex = 10
        loseText.zIndex = 10

        unmuteButton.hide = true
        muteButton.zIndex = 5
        unmuteButton.zIndex = 5
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
            menuController?.die()
            menuController = null
            healthIndicator = HealthIndicator()
        }
        when (newGameState) {
            GameState.ACTIVE_GAME -> actionPanel = ActiveGameActionPanel()
            GameState.NONE -> {}
            GameState.PLANNING_DEFENSE -> actionPanel = PlanningDefenseActionPanel()
            GameState.PLANNING_ATTACK -> actionPanel = PlanningAttackActionPanel()
            GameState.START_MENU -> menuController = MenuController()
        }
    }

    fun showWinUI() {
        println("HUDManager::showWinUI")
        winText.hide = false
    }

    fun showLoseUI() {
        println("HUDManager::showLoseUI")
        loseText.hide = false
        healthIndicator?.let { it.health -= 1 }
    }

    private fun onMuteButtonClicked() {
        AudioManager.mute = true
        muteButton.hide = true
        unmuteButton.hide = false
    }

    private fun onUnmuteButtonClicked() {
        AudioManager.mute = false
        muteButton.hide = false
        unmuteButton.hide = true
    }

}
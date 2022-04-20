package com.cows.game.hud
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.cows.game.enums.GameState
import com.cows.game.enums.TowerType
import com.cows.game.gameState.GameStateSubscriber
import com.cows.game.managers.FunctionDelayer
import com.cows.game.managers.TowerSpawner


// Should manage which naviagations buttons that should be displayed
class HUDManager(private val onStartGame: () -> Unit): GameStateSubscriber() {
    //private val buttons = mutableListOf<Button>()
    private lateinit var actionPanel: ActionPanel


    override fun onChangeGameState(oldGameState: GameState, newGameState: GameState) {
        println("Changing game state from $oldGameState to $newGameState")
        //buttons.forEach { it.die() }
        //buttons.clear()
        when (newGameState) {
            GameState.ACTIVE_GAME -> {
                actionPanel = ActiveGameActionPanel()
                //createActiveGameButtons()
            }
            GameState.NONE -> {}
            GameState.START_MENU -> actionPanel = StartMenuActionPanel()
            GameState.PLANNING_DEFENSE -> {
                actionPanel = PlanningDefenseActionPanel {onStartGame.invoke()}
                //createPlanningDefenseButtons()
            }
            GameState.PLANNING_ATTACK -> {
                actionPanel = PlanningAttackActionPanel {onStartGame.invoke()}
                //createPlanningAttackButtons()
            }
        }
    }

/**
    private fun createPlanningDefenseButtons() {
        val startGameButton = Button("HUD/start-button.png") { onStartGame.invoke() }
        val cancelPlacementButton = Button("HUD/cancel-button.png", Vector2(Gdx.graphics.width - 150f, 30f))

        fun cancelPlacement() {
            TowerSpawner.cancelPlacement()
            FunctionDelayer.invokeFunctionAtEndOfThisFrame { buttons.forEach { it.hide = it == cancelPlacementButton } }
        }

        fun selectTower(type: TowerType) {
            TowerSpawner.selectTower(type) { cancelPlacement() }
            FunctionDelayer.invokeFunctionAtEndOfThisFrame { buttons.forEach { it.hide = it != cancelPlacementButton } }
        }

        cancelPlacementButton.hide = true
        cancelPlacementButton.onClick = { cancelPlacement() }

        val pos = Vector2(Gdx.graphics.width - 3 * 106f, 0f) // 106 is currently the width of each sprite. change this later
        val spawnTowerButton1 = Button("Towers/tower1.png", Vector2(pos.x, pos.y))
        val spawnTowerButton2 = Button("Towers/tower2.png", Vector2(pos.x + 106, pos.y))
        val spawnTowerButton3 = Button("Towers/tower3.png", Vector2(pos.x + 106 * 2, pos.y))

        spawnTowerButton1.onClick = { selectTower(TowerType.WOOD) }
        spawnTowerButton2.onClick = { selectTower(TowerType.STONE) }
        spawnTowerButton3.onClick = { selectTower(TowerType.SOMETHING) }

        buttons.add(startGameButton)
        buttons.add(cancelPlacementButton)
        buttons.add(spawnTowerButton1)
        buttons.add(spawnTowerButton2)
        buttons.add(spawnTowerButton3)
    }

    private fun createPlanningAttackButtons() {
        val startGameButton = Button("HUD/start-button.png") { onStartGame.invoke() }
        val cancelPlacementButton = Button("HUD/cancel-button.png", Vector2(Gdx.graphics.width - 150f, 30f))
        buttons.add(cancelPlacementButton)
        buttons.add(startGameButton)
    }

    private fun createActiveGameButtons() {
        println("Creating Game Buttons")
    }
**/
}
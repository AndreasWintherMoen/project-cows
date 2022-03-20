package com.cows.game.hud
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.cows.game.enums.GameState
import com.cows.game.gameState.GameStateSubscriber


// Should manage which naviagations buttons that should be displayed
class HUDManager(private val onStartGame: () -> Unit): GameStateSubscriber() {
    private val buttons = mutableListOf<Button>()

    override fun onChangeGameState(oldGameState: GameState, newGameState: GameState) {
        println("Changing game state from $oldGameState to $newGameState")

        buttons.forEach { it.die() }
        buttons.clear()
        when (newGameState) {
            GameState.ACTIVE_GAME -> createActiveGameButtons()
            GameState.NONE -> {}
            GameState.START_MENU -> {}
            GameState.PLANNING_DEFENSE -> createPlanningDefenseButtons()
            GameState.PLANNING_ATTACK -> createPlanningAttackButtons()
        }
    }

    private fun createPlanningDefenseButtons() {
        val startGameButton = Button("HUD/start-button.png", { onStartGame.invoke() })

        val pos = Vector2(Gdx.graphics.width - 3 * 106f, 0f) // 106 is currently the width of each sprite. change this later
        val spawnTowerButton1 = Button("Towers/tower1.png", Vector2(pos.x, pos.y)) { println("Spawning tower 1") }
        val spawnTowerButton2 = Button("Towers/tower2.png", Vector2(pos.x + 106, pos.y)) { println("Spawning tower 2") }
        val spawnTowerButton3 = Button("Towers/tower3.png", Vector2(pos.x + 106 * 2, pos.y)) {
            println(
                "Spawning tower 3"
            )
        }

        buttons.add(startGameButton)
        buttons.add(spawnTowerButton1)
        buttons.add(spawnTowerButton2)
        buttons.add(spawnTowerButton3)
    }

    private fun createPlanningAttackButtons() {
        val startGameButton = Button("HUD/start-button.png", { onStartGame.invoke() })

        buttons.add(startGameButton)
    }

    private fun createActiveGameButtons() {

    }
}
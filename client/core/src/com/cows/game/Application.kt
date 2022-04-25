package com.cows.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.cows.game.enums.GameState
import com.cows.game.hud.ActionPanel
import com.cows.game.hud.HUDManager
import com.cows.game.managers.*
import com.cows.game.map.Map
import com.cows.game.models.TileModel
import com.cows.game.roundSimulation.GameTickProcessor
import com.cows.game.roundSimulation.rawJsonData.JsonRoundSimulation
import com.cows.game.serverConnection.ServerConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktx.async.KtxAsync

class Application : ApplicationAdapter()  {
    companion object {
        const val WIDTH = Map.WIDTH * TileModel.WIDTH + ActionPanel.WIDTH
        const val HEIGHT = Map.HEIGHT * TileModel.HEIGHT
    }
    private var gameTickProcessor: GameTickProcessor? = null
    private lateinit var hudManager: HUDManager

    override fun create() {
        KtxAsync.initiate()

        KtxAsync.launch{

            launch {
                Redux.init()
                AudioManager.init()
                RoundManager.init()
                hudManager = HUDManager()
                GameStateManager.currentGameState = GameState.START_MENU
            }
        }
    }

    override fun render() {
        loadReduxValues()

        val tickDuration = if (RoundManager.useFastForward) 0.05f else 0.2f
        val deltaTime = Gdx.graphics.deltaTime
        val tickAdjustedDeltaTime = deltaTime / tickDuration

        if (GameStateManager.currentGameState == GameState.ACTIVE_GAME) {
            gameTickProcessor?.update(deltaTime, tickDuration)
        }

        Updater.update(tickAdjustedDeltaTime)
        Renderer.render(tickAdjustedDeltaTime)
        FunctionDelayer.invokeRegisteredFunctions()
        GameStateManager.nextAsyncGameState?.let { GameStateManager.currentGameState = it; GameStateManager.nextAsyncGameState = null }
    }

    override fun dispose() {
        Renderer.dispose()
    }

    private fun loadReduxValues() {
        Redux.jsonRoundSimulation?.let {
            startGame(it);
            RoundManager.reloadReduxValues()
            Redux.jsonRoundSimulation = null
        }
        Redux.playerCreatedGame?.let {
            println("Setting playerCreatedGame to $it")
            RoundManager.playerCreatedGame = it
            Redux.playerCreatedGame = null
        }
        Redux.errorMessage?.let {
            hudManager.onError(it)
            Redux.errorMessage = null
        }
    }

    private fun startGame(roundSimulation: JsonRoundSimulation) {
        if (GameStateManager.currentGameState == GameState.ACTIVE_GAME) return
        println("startGame")
        GameStateManager.currentGameState = GameState.ACTIVE_GAME
        gameTickProcessor = GameTickProcessor(roundSimulation) { finishGame() }
    }

    private fun finishGame() {
        println("Application::finishGame")
        gameTickProcessor?.killAllUnits()
        RoundManager.roundSimulation?.let {
            val playerWon = !it.attackerWon.xor(RoundManager.playerIsAttacker)
            if (playerWon) {
                hudManager.showWinUI()
                AudioManager.playMusic("Sound/victory.mp3")
            }
            else {
                hudManager.showLoseUI()
                // TODO: Add lose sound
                AudioManager.playMusic("Sound/victory.mp3")
            }
        }
        GlobalScope.launch(Dispatchers.IO) {
            delay(5000)
            println("Starting coroutine context")
            RoundManager.gameStatus = ServerConnection.getGameStatus()
            gameTickProcessor = null
            if (RoundManager.playerIsAttacker) {
                GameStateManager.setGameStateAsync(GameState.PLANNING_DEFENSE)
            }
            else {
                GameStateManager.setGameStateAsync(GameState.PLANNING_ATTACK)
            }
        }
    }

}
package com.cows.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.cows.game.enums.GameState
import com.cows.game.hud.ActionPanel
import com.cows.game.hud.HUDManager
import com.cows.game.hud.PlanningAttackActionPanel
import com.cows.game.managers.*
import com.cows.game.map.Map
import com.cows.game.models.TileModel
import com.cows.game.roundSimulation.GameTickProcessor
import com.cows.game.roundSimulation.RoundSimulationDeserializer
import com.cows.game.roundSimulation.rawJsonData.JsonRoundSimulation
import com.cows.game.serverConnection.ServerConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import ktx.async.KtxAsync

class Application : ApplicationAdapter()  {
    companion object {
        const val WIDTH = Map.WIDTH * TileModel.WIDTH + ActionPanel.WIDTH
        const val HEIGHT = Map.HEIGHT * TileModel.HEIGHT
    }
    val tickDuration = 0.2f // in seconds
    private var gameTickProcessor: GameTickProcessor? = null
    private lateinit var hudManager: HUDManager
    private var playerIsAttacker = false

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
        Redux.jsonRoundSimulation?.let { println("json round simulation is not null"); startGame(it); Redux.jsonRoundSimulation = null }

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

    private fun startGame(roundSimulation: JsonRoundSimulation) {
        if (GameStateManager.currentGameState == GameState.ACTIVE_GAME) return
        println("startGame")
        GameStateManager.currentGameState = GameState.ACTIVE_GAME
        gameTickProcessor = GameTickProcessor(roundSimulation) { finishGame() }
    }

    private fun finishGame() {
        println("Application::finishGame")
        gameTickProcessor?.killAllUnits()
        Redux.jsonRoundSimulation?.let {
            val playerWon = !it.attackerWon.xor(RoundManager.playerIsAttacker)
            if (playerWon) hudManager.showWinText()
            else hudManager.showLoseText()
        }
        println("Hei")
        GlobalScope.launch(Dispatchers.IO) {
            println("Starting coroutine context")
            Redux.gameStatus = ServerConnection.getGameStatus()
            println(Redux.gameStatus)
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
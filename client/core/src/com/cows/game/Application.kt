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
import kotlinx.coroutines.launch
import java.io.File
import ktx.async.KtxAsync

class Application : ApplicationAdapter()  {
    companion object {
        const val WIDTH = Map.WIDTH * TileModel.WIDTH + ActionPanel.WIDTH
        const val HEIGHT = Map.HEIGHT * TileModel.HEIGHT
    }
    val tickDuration = 0.2f // in seconds
    private lateinit var gameTickProcessor: GameTickProcessor
    private lateinit var hudManager: HUDManager

    override fun create() {
        KtxAsync.initiate()

        KtxAsync.launch{

            launch {
                Redux.init()
                AudioManager.init()
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
            gameTickProcessor.update(deltaTime, tickDuration)
        }

        Updater.update(tickAdjustedDeltaTime)
        Renderer.render(tickAdjustedDeltaTime)
        FunctionDelayer.invokeRegisteredFunctions()
        GameStateManager.nextAsyncGameState?.let { println(Redux.jsonAvailableTowers); GameStateManager.currentGameState = it; GameStateManager.nextAsyncGameState = null }
    }

    override fun dispose() {
        Renderer.dispose()
    }

    private fun startGame(roundSimulation: JsonRoundSimulation) {
        if (GameStateManager.currentGameState == GameState.ACTIVE_GAME) return
        println("startGame")
        GameStateManager.currentGameState = GameState.ACTIVE_GAME
        gameTickProcessor = GameTickProcessor(roundSimulation)
    }

    private fun loadRoundSimulation() {
        val parsedFile = File("roundSimulation.json").readText()
        val roundSimulation = RoundSimulationDeserializer.deserialize(parsedFile)
        println("parsed JSON simulation object: $roundSimulation")
        gameTickProcessor = GameTickProcessor(roundSimulation)
    }

}
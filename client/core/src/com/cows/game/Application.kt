package com.cows.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.cows.game.enums.GameState
import com.cows.game.hud.HUDManager
import com.cows.game.managers.FunctionDelayer
import com.cows.game.managers.GameStateManager
import com.cows.game.managers.Renderer
import com.cows.game.managers.Updater
import com.cows.game.map.Map
import com.cows.game.models.TileModel
import com.cows.game.roundSimulation.GameTickProcessor
import com.cows.game.roundSimulation.RoundSimulationDeserializer
import com.cows.game.serverConnection.ServerConnection
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class Application : ApplicationAdapter()  {
    companion object {
        const val WIDTH = Map.WIDTH * TileModel.WIDTH
        const val HEIGHT = Map.HEIGHT * TileModel.HEIGHT
    }

    val tickDuration = 1f // in seconds
    private lateinit var gameTickProcessor: GameTickProcessor
    private lateinit var hudManager: HUDManager

    override fun create() {
        Map.init()
        hudManager = HUDManager { startGame() }
        GameStateManager.currentGameState = GameState.PLANNING_DEFENSE
    }

    override fun render() {
        val deltaTime = Gdx.graphics.deltaTime
        val tickAdjustedDeltaTime = deltaTime / tickDuration

        if (GameStateManager.currentGameState == GameState.ACTIVE_GAME) {
            gameTickProcessor.update(deltaTime, tickDuration)
        }

        Updater.update(tickAdjustedDeltaTime)
        Renderer.render(tickAdjustedDeltaTime)

        FunctionDelayer.invokeRegisteredFunctions()
    }

    override fun dispose() {
        Renderer.dispose()
    }

    private fun startGame() {
        println("Starting game")
        GameStateManager.currentGameState = GameState.ACTIVE_GAME
        loadRoundSimulation()
    }

    // this will load from API at some point
    private fun loadRoundSimulation() {
        val parsedFile = File("roundSimulation.json").readText()
        val roundSimulation = RoundSimulationDeserializer.deserialize(parsedFile)
        println("parsed JSON simulation object: $roundSimulation")
        gameTickProcessor = GameTickProcessor(roundSimulation)
    }

}
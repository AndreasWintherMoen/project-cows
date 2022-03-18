package com.cows.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.cows.game.enums.GameState
import com.cows.game.hud.StartGameButton
import com.cows.game.models.TileModel
import com.cows.game.roundSimulation.GameTickProcessor
import com.cows.game.roundSimulation.RoundSimulationDeserializer
import java.io.File

class Application : ApplicationAdapter() {
    companion object {
        const val WIDTH = Map.WIDTH * TileModel.WIDTH
        const val HEIGHT = Map.HEIGHT * TileModel.HEIGHT
        const val TICK_DURATION = 1f // in seconds
        var gameState = GameState.PLANNING_DEFENSE
    }
    private lateinit var startButton: StartGameButton

    val tickDuration = 0.5f // in seconds
    private lateinit var gameTickProcessor: GameTickProcessor

    override fun create() {
        Map.init()
        startButton = StartGameButton { startGame() }
    }

    override fun render() {
        val deltaTime = Gdx.graphics.deltaTime
        val tickAdjustedDeltaTime = deltaTime / tickDuration

        if (gameState != GameState.ACTIVE_GAME){
            Renderer.render(tickAdjustedDeltaTime)
            return
        }

        gameTickProcessor.update(deltaTime, tickDuration)

        Updater.update(tickAdjustedDeltaTime)
        Renderer.render(tickAdjustedDeltaTime)
    }

    override fun dispose() {
        Renderer.dispose()
    }

    private fun startGame() {
        gameState = GameState.ACTIVE_GAME
        val parsedFile = File("roundSimulation.json").readText()
        val roundSimulation = RoundSimulationDeserializer.deserialize(parsedFile)
        println("parsed JSON simulation object: $roundSimulation")
        gameTickProcessor = GameTickProcessor(roundSimulation)
    }

}
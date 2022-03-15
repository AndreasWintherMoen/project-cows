package com.cows.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.cows.game.client.client
import com.cows.game.models.TileModel
import com.cows.game.roundSimulation.GameLoopSimulator
import com.cows.game.roundSimulation.RoundSimulationDeserializer
import java.io.File

class Application : ApplicationAdapter()  {
    companion object {
        const val WIDTH = Map.WIDTH * TileModel.WIDTH
        const val HEIGHT = Map.HEIGHT * TileModel.HEIGHT
        const val TICK_DURATION = 1f // in seconds
    }

    private lateinit var gameLoopSimulator: GameLoopSimulator

    override fun create() {
        Map.init()
        var client = client()
        val parsedFile = File("roundSimulation.json").readText()
        val roundSimulation = RoundSimulationDeserializer.deserialize(parsedFile)
        println("parsed JSON simulation object: $roundSimulation")
        gameLoopSimulator = GameLoopSimulator(roundSimulation)
    }

    override fun render() {
        val deltaTime = Gdx.graphics.deltaTime
        gameLoopSimulator.update(deltaTime)
        Renderer.render(deltaTime)
    }

    override fun dispose() {
        Renderer.dispose()
    }
}
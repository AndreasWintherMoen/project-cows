package com.cows.game

import com.badlogic.gdx.ApplicationAdapter
import com.cows.game.models.Tile
import com.cows.game.roundSimulation.GameLoopSimulator
import com.cows.game.roundSimulation.RoundSimulationDeserializer
import java.io.File

class Application : ApplicationAdapter() {
    companion object {
        const val WIDTH = Map.WIDTH * Tile.WIDTH
        const val HEIGHT = Map.HEIGHT * Tile.HEIGHT
        fun readRoundSimFileAsString(fileName: String): String = File(fileName).readText()
    }

    private lateinit var map: Map
    private lateinit var gameLoopSimulator: GameLoopSimulator

    override fun create() {
        map = Map()
        val parsedFile = readRoundSimFileAsString("roundSimulation.json")
        val roundSimulation = RoundSimulationDeserializer.deserialize(parsedFile)
        gameLoopSimulator = GameLoopSimulator(roundSimulation)
    }

    override fun render() {
        Renderer.instance.render()
    }

    override fun dispose() {
        Renderer.instance.dispose()
    }
}
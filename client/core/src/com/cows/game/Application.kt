package com.cows.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.cows.game.models.Tile

class Application : ApplicationAdapter() {
    companion object {
        const val WIDTH = Map.WIDTH * Tile.WIDTH
        const val HEIGHT = Map.HEIGHT * Tile.HEIGHT
    }

    private lateinit var map: Map

    override fun create() {
        map = Map()
    }

    override fun render() {
        Renderer.instance.render()
    }

    override fun dispose() {
        Renderer.instance.dispose()
    }
}
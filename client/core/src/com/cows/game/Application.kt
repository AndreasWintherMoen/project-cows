package com.cows.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

class Application : ApplicationAdapter() {
    companion object {
        const val WIDTH = 1024
        const val HEIGHT = 1024
    }
    override fun create() {
    }

    override fun render() {
        Renderer.instance.render()
    }

    override fun dispose() {
        Renderer.instance.dispose()
    }
}
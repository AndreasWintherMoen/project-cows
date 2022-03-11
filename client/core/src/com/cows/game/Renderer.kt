package com.cows.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.cows.game.views.IRenderable

class Renderer {
    companion object {
        val instance = Renderer()
    }

    private val batch = SpriteBatch()
    private val renderables = mutableListOf<IRenderable>()
    private val cam = OrthographicCamera()

    init {
        cam.setToOrtho(false, Application.WIDTH.toFloat(), Application.HEIGHT.toFloat())
    }

    fun render() {
        val deltaTime = Gdx.graphics.deltaTime
        ScreenUtils.clear(0f, 0f, 0f, 1f)
        batch.projectionMatrix = cam.combined
        batch.begin()
        renderables.forEach { it.render(batch, deltaTime) }
        batch.end()
    }

    fun dispose() {
        batch.dispose()
        renderables.forEach { it.dispose() }
    }

    fun addRenderable(renderable: IRenderable) = renderables.add(renderable)

    // TODO: Check if exists???
    fun removeRenderable(renderable: IRenderable) = renderables.remove(renderable)
}
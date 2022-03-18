package com.cows.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.cows.game.views.Renderable

object Renderer {

    private val batch = SpriteBatch()
    private val renderables = mutableListOf<Renderable>()
    private val newRenderables = mutableListOf<Renderable>()
    private val cam = OrthographicCamera()


    init {
        cam.setToOrtho(false, Application.WIDTH, Application.HEIGHT)
    }

    fun render(deltaTime: Float) {
        ScreenUtils.clear(0f, 0f, 0f, 1f)
        batch.projectionMatrix = cam.combined
        batch.begin()
        renderables.forEach { it.render(batch, deltaTime) }
        batch.end()

        newRenderables.forEach { renderables.add(it) }
        newRenderables.clear()
    }

    fun dispose() {
        batch.dispose()
        renderables.forEach { it.dispose() }
    }

    fun addRenderable(renderable: Renderable) = newRenderables.add(renderable)

        // TODO: Check if exists???
        fun removeRenderable(renderable: Renderable) = renderables.remove(renderable)
}
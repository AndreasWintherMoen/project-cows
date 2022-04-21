package com.cows.game.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.cows.game.Application
import com.cows.game.views.Renderable

object Renderer {

    private val batch = SpriteBatch()
    private val renderables = mutableListOf<Renderable>()
    private val renderablesToBeAdded = mutableListOf<Renderable>()
    private val renderablesToBeRemoved = mutableListOf<Renderable>()
    private val cam = OrthographicCamera()
    val viewport = FitViewport(Application.WIDTH, Application.HEIGHT, cam)
    val stage = Stage(viewport, batch)

    init {
        cam.setToOrtho(false, Application.WIDTH, Application.HEIGHT)
        Gdx.input.inputProcessor = stage
    }

    fun render(deltaTime: Float) {
        ScreenUtils.clear(0f, 0f, 0f, 1f)
        stage.batch.projectionMatrix = stage.camera.combined
        stage.camera.update()

        stage.viewport.apply(true)

        stage.batch.begin()
        renderables.forEach { if (!it.hide) it.render(batch, deltaTime) }
        stage.batch.end()

        renderablesToBeAdded.forEach { renderables.add(it) }
        renderablesToBeAdded.clear()
        renderablesToBeRemoved.forEach { renderables.remove(it) }
        renderablesToBeRemoved.clear()
    }

    fun dispose() {
        batch.dispose()
        renderables.forEach { it.dispose() }
    }

    fun addRenderable(renderable: Renderable) = renderablesToBeAdded.add(renderable)

    fun removeRenderable(renderable: Renderable) = renderablesToBeRemoved.add(renderable)
}
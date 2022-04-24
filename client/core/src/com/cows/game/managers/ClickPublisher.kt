package com.cows.game.managers

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.cows.game.Application
import com.cows.game.ClickSubscriber
import com.cows.game.controllers.Updatable
import com.cows.game.enums.GameState
import com.cows.game.map.Map

object ClickPublisher: Updatable() {
    private val subscribers = mutableListOf<ClickSubscriber>()
    private val subscribersToBeAdded = mutableListOf<ClickSubscriber>()
    private val subscribersToBeRemoved = mutableListOf<ClickSubscriber>()

    fun subscribeToClickEvents(subscriber: ClickSubscriber) = subscribersToBeAdded.add(subscriber)
    fun unsubscribeToClickEvents(subscriber: ClickSubscriber) = subscribersToBeRemoved.add(subscriber)

    override fun update(deltaTime: Float) {

        subscribersToBeAdded.forEach { subscribers.add(it) }
        subscribersToBeAdded.clear()
        subscribersToBeRemoved.forEach { subscribers.remove(it) }
        subscribersToBeRemoved.clear()

        if (!Gdx.input.justTouched()) return

        println(Gdx.graphics.height)
        println(Renderer.viewport.screenHeight)
        println(Renderer.viewport.screenY)
        println(Gdx.input.y.toFloat())
        val scaleFactor = Vector2(Application.WIDTH / Renderer.viewport.screenWidth, Application.HEIGHT / Renderer.viewport.screenHeight)
        val clickedPosition = Vector2(
            Gdx.input.x.toFloat() * scaleFactor.x - Renderer.viewport.leftGutterWidth,
            (Renderer.viewport.screenHeight - Gdx.input.y.toFloat()) * scaleFactor.y - Renderer.viewport.topGutterHeight)
        println("clickedPosition: $clickedPosition")

        try {
            val clickedTile = if (GameStateManager.currentGameState == GameState.START_MENU) null else Map.getTileAtPixel(clickedPosition)
            subscribers.forEach { it.click(clickedPosition, clickedTile) }
        } catch (err: Error) {
            subscribers.forEach { it.click(clickedPosition, null) }
        }
    }
}
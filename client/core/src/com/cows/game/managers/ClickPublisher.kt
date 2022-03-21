package com.cows.game.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.cows.game.ClickSubscriber
import com.cows.game.controllers.Updatable
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

        val clickedPosition = Vector2(Gdx.input.x.toFloat(), Gdx.graphics.height - Gdx.input.y.toFloat())

        try {
            val clickedTile = Map.getTileAtPixel(clickedPosition)
            subscribers.forEach { it.click(clickedPosition, clickedTile) }
        } catch (err: Error) {
            subscribers.forEach { it.click(clickedPosition, null) }
        }
    }
}
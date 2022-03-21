package com.cows.game

import com.badlogic.gdx.math.Vector2
import com.cows.game.controllers.TileController
import com.cows.game.managers.ClickPublisher

interface ClickSubscriber {
    fun subscribeToClickEvents() { ClickPublisher.subscribeToClickEvents(this) }
    fun unsubscribeToClickEvents() { ClickPublisher.unsubscribeToClickEvents(this) }
    fun click(position: Vector2, tile: TileController?)
}
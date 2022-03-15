package com.cows.game.controllers

import com.cows.game.Coordinate
import com.cows.game.enums.TileType
import com.cows.game.models.TileModel
import com.cows.game.views.TileView

class TileController(tileType: TileType, coordinate: Coordinate) : Updatable() {
    private val tileModel = TileModel(tileType, coordinate)
    override fun update(deltaTime: Float) {
        TODO("Not yet implemented")
    }

    override val renderableView = TileView(tileModel)
}
package com.cows.game.controllers

import com.cows.game.map.Coordinate
import com.cows.game.enums.TileType
import com.cows.game.models.TileModel
import com.cows.game.views.TileView


class TileController(tileType: TileType, coordinate: Coordinate) : Updatable() {
    val tileModel = TileModel(tileType, coordinate)
    val tileView = TileView(tileModel)

    override fun update(deltaTime: Float) {
       // TODO("Not yet implemented")
    }
}


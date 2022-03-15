package com.cows.game.controllers

import com.cows.game.Coordinate
import com.cows.game.enums.TileType
import com.cows.game.models.TileModel
import com.cows.game.views.TileView

class TileController(tileType: TileType, coordinate: Coordinate) {
    private val tileModel = TileModel(tileType, coordinate)
    private val tileView = TileView(tileModel)
}
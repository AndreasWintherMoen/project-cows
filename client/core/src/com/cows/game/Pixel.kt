package com.cows.game

import com.cows.game.models.Tile

data class Pixel(var x: Float, var y: Float) {
    fun toCoordinate() = Coordinate((x / Tile.WIDTH).toInt(), (y / Tile.HEIGHT).toInt())
}

package com.cows.game

import com.cows.game.models.Tile

data class Coordinate(var x: Int, var y: Int) {
    fun toPixel() = Pixel(x * Tile.WIDTH, y * Tile.HEIGHT)
}

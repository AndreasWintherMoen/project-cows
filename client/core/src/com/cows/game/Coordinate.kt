package com.cows.game

import com.cows.game.models.Tile

data class Coordinate(var x: Int, var y: Int) {
    constructor() : this(0, 0)
    fun toPixel() = Pixel(x * Tile.WIDTH, y * Tile.HEIGHT)
}

package com.cows.game

import com.badlogic.gdx.math.Vector2
import com.cows.game.models.TileModel

data class Coordinate(var x: Int, var y: Int) {
    fun toVector2() = Vector2(x * TileModel.WIDTH, y * TileModel.HEIGHT)

    // operator overloading
    operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
    operator fun minus(other: Coordinate) = Coordinate(x - other.x, y - other.y)
    operator fun times(other: Coordinate) = Coordinate(x * other.x, y * other.y)
}

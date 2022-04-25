package models.generation

import org.mini2Dx.gdx.math.Vector2
import models.simulation.TileModel

data class Coordinate(var x: Int, var y: Int) {
    fun toVector2() = Vector2(x * TileModel.WIDTH, y * TileModel.HEIGHT)

    // operator overloading
    operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
    operator fun minus(other: Coordinate) = Coordinate(x - other.x, y - other.y)
    operator fun times(other: Coordinate) = Coordinate(x * other.x, y * other.y)
}

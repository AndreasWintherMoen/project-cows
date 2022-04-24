package projectcows.models

import org.mini2Dx.gdx.math.Vector2

data class Coordinate(var x: Int, var y: Int) {
    // operator overloading
    operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
    operator fun minus(other: Coordinate) = Coordinate(x - other.x, y - other.y)
    operator fun times(other: Coordinate) = Coordinate(x * other.x, y * other.y)
}

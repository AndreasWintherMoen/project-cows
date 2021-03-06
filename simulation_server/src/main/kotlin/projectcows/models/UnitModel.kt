package projectcows.models

import projectcows.enums.UnitType
import org.mini2Dx.gdx.math.Vector2

data class UnitModel (
    val type: UnitType,
    val level: Int,
    var position: Vector2,
    val rotation: Float,
    var isDead: Boolean,
    var currentDirection: Coordinate,
    var movementSpeed: Float
)




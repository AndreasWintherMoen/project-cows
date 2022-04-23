package projectcows.models

import com.cows.services.simulation.enums.UnitType

data class TowerModel (
    val type: UnitType,
    val tileCoordinate: Coordinate,
    var rotation: Float,
    var hasTarget: Boolean

) {
    constructor(type: UnitType, tileCoordinate: Coordinate) : this(type, tileCoordinate, 0f, false)
}

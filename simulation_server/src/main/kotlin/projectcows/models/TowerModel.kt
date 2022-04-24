package projectcows.models

import projectcows.enums.UnitType

data class TowerModel (
    val type: UnitType,
    val level: Int,
    val tileCoordinate: Coordinate,
    var rotation: Float,
    var hasTarget: Boolean
)

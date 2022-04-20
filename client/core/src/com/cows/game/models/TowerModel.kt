package com.cows.game.models

import com.cows.game.map.Coordinate
import com.cows.game.enums.UnitType

data class TowerModel (
    val type: UnitType,
    val tileCoordinate: Coordinate,
    var rotation: Float,
    var hasTarget: Boolean

) {
    constructor(type: UnitType, tileCoordinate: Coordinate) : this(type, tileCoordinate, 0f, false)
}

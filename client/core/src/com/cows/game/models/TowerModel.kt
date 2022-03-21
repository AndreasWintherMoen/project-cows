package com.cows.game.models

import com.cows.game.map.Coordinate
import com.cows.game.enums.TowerType

data class TowerModel (
    val type: TowerType,
    val tileCoordinate: Coordinate,
    var rotation: Float,
    var hasTarget: Boolean

) {
    constructor(type: TowerType, tileCoordinate: Coordinate) : this(type, tileCoordinate, 0f, false)
}

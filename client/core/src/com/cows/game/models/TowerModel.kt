package com.cows.game.models

import com.cows.game.map.Coordinate
import com.cows.game.enums.UnitType

data class TowerModel (
    val type: UnitType,
    val level: Int,
    val tileCoordinate: Coordinate,
    var rotation: Float,
    var hasTarget: Boolean,
    val range: Int,
    val damage: Int

) {
    constructor(type: UnitType, level: Int, tileCoordinate: Coordinate, range: Int, damage: Int) : this(type, level, tileCoordinate, 0f, false, range, damage)
}

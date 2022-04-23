package com.cows.game.models

import com.badlogic.gdx.math.Vector2
import com.cows.game.map.Coordinate
import com.cows.game.enums.UnitType

data class UnitModel(
    val type: UnitType,
    val level: Int,
    var position: Vector2,
    val rotation: Float,
    var isDead: Boolean,
    var currentDirection: Coordinate,
    var movementSpeed: Float,
    val health: Int,
) {
    constructor(type: UnitType, level: Int, movementSpeed: Float, health: Int) : this(type, level, Vector2(0f, 0f), 0f, false, Coordinate(0,0), movementSpeed, health)
}

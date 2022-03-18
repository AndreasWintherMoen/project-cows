package com.cows.game.models

import com.badlogic.gdx.math.Vector2
import com.cows.game.Coordinate
import com.cows.game.enums.UnitType

data class UnitModel (
    val type: UnitType,
    var position: Vector2,
    val rotation: Float,
    var isDead: Boolean,
    var currentDirection: Coordinate,
    var movementSpeed: Float
) {
    constructor(type: UnitType, movementSpeed: Float) : this(type, Vector2(0f, 0f), 0f, false, Coordinate(0,0), movementSpeed)
}




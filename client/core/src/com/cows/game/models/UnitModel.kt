package com.cows.game.models

import com.badlogic.gdx.math.Vector2
import com.cows.game.enums.UnitType

data class UnitModel (
    val type: UnitType,
    var position: Vector2,
    val rotation: Float,
) {
    constructor(type: UnitType) : this(type, Vector2(0f, 0f), 0f)
}


package com.cows.game.models

import com.badlogic.gdx.math.Vector2

data class BulletModel (
    var position: Vector2,
    val movementSpeed: Float,
    var hasHit: Boolean,
    var destination: Vector2,
    var rotation: Float
    ) {
    constructor(position: Vector2) : this(position,5f, false, Vector2(0f,0f), 0f)
}
package models.simulation

import org.mini2Dx.gdx.math.Vector2

data class BulletModel (
    var position: Vector2,
    val movementSpeed: Float,
    var hasHit: Boolean,
    ) {
    constructor(position: Vector2) : this(position,8f, false)
}
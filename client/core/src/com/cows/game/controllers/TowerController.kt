package com.cows.game.controllers

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.cows.game.models.BulletModel
import com.cows.game.models.TowerModel
import com.cows.game.views.TowerView

class TowerController(val model: TowerModel): Updatable() {
    private var currentTarget: UnitController? = null
    private var currentBullet: BulletController? = null

    override fun update(deltaTime: Float) {
        if (currentTarget == null) return

        val targetPos = currentTarget!!.model.position
        val startPos = model.tileCoordinate.toVector2()
        val directionToTarget = Vector2(targetPos.x - startPos.x, targetPos.y - startPos.y)
        model.rotation = MathUtils.atan2(directionToTarget.y, directionToTarget.x) * MathUtils.radDeg

        currentBullet = BulletController(BulletModel(model.tileCoordinate.toVector2()))

    }

    val renderableView = TowerView(model)

    fun target(unit: UnitController) {
        currentTarget = unit
        model.hasTarget = true
    }

    fun removeTarget() {
        currentTarget = null
        model.hasTarget = false
    }

    fun attack() {
        currentBullet?.attack(currentTarget, model.tileCoordinate.toVector2())
    }
}
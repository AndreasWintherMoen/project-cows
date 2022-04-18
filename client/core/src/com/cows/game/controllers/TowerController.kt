package com.cows.game.controllers

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.cows.game.models.TowerModel
import com.cows.game.views.TowerView
import kotlin.math.sin

class TowerController(val model: TowerModel): Updatable() {
    private var currentTarget: UnitController? = null
    private var currentBullet: BulletController? = null

    override fun update(deltaTime: Float) {
        if (currentTarget == null) return

        val targetPos = currentTarget!!.model.position
        val startPos = model.tileCoordinate.toVector2()
        val directionToTarget = Vector2(targetPos.x - startPos.x, targetPos.y - startPos.y)
        model.rotation = MathUtils.atan2(directionToTarget.y, directionToTarget.x) * MathUtils.radDeg
        currentBullet?.model?.rotation = model.rotation

        currentBullet?.model?.destination = targetPos
        currentBullet?.update(deltaTime)
    }

    val renderableView = TowerView(model)


    fun target(unit: UnitController, bulletController: BulletController) {
        currentTarget = unit
        model.hasTarget = true

        currentBullet = bulletController

    }

    fun removeTarget() {
        currentTarget = null
        model.hasTarget = false
        currentBullet?.die()

    }

    fun attack() {
        currentBullet?.attack()
    }
}
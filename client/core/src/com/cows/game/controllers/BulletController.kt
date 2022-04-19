package com.cows.game.controllers

import com.badlogic.gdx.math.Vector2
import com.cows.game.models.BulletModel
import com.cows.game.views.BulletView
import kotlin.math.roundToInt

class BulletController(private val model: BulletModel): Updatable() {

    private var currentTarget: UnitController? = null


    override fun update(deltaTime: Float) {
        if (model.hasHit) return
        if (currentTarget == null) return
        val targetPos = currentTarget!!.model.position
        val oldX = roundNum(model.position.x)
        val oldY = roundNum(model.position.y)

        model.position.x += ((targetPos.x + 32f - model.position.x)) / model.movementSpeed
        model.position.y += ((targetPos.y + 32f - model.position.y))/ model.movementSpeed

        if (oldX == roundNum(model.position.x)) die()
        if (oldY == roundNum(model.position.y)) die()
    }

    fun attack(unit: UnitController?, start: Vector2) {
        if (unit != null) {
            currentTarget = unit
            model.position = start.add(32f,32f)
            BulletView(model)
        }
    }

    override fun die() {
        super.die()
        model.hasHit = true
    }

    private fun roundNum(f: Float): Int {
        return (f * 10000).roundToInt() / 10000
    }


}
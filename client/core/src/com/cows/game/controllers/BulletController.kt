package com.cows.game.controllers

import com.cows.game.models.BulletModel
import com.cows.game.views.BulletView

class BulletController(val model: BulletModel): Updatable() {
    private lateinit var view: BulletView

    override fun update(deltaTime: Float) {
        if (model.hasHit) return
        else {
            model.position.x += (model.destination.x - model.position.x) / model.movementSpeed
            model.position.y += (model.destination.y - model.position.y) / model.movementSpeed
        }
        view = BulletView(model)

    }

    fun attack() {
        model.hasHit = true
    }

    override fun die() {
        super.die()
        model.hasHit = true
    }

    fun spawn() {

        view = BulletView(model)

    }
}
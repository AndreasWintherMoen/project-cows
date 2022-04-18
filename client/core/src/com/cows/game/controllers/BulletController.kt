package com.cows.game.controllers

import com.badlogic.gdx.math.Vector2
import com.cows.game.models.BulletModel
import com.cows.game.views.BulletView

class BulletController(val model: BulletModel): Updatable() {
    private lateinit var view: BulletView

    override fun update(deltaTime: Float) {
        if (model.hasHit) {
            model.position.x = model.destination.x
            model.position.y = model.destination.y
        } else {
            view = BulletView(model)
            model.position.x += (model.destination.x - model.position.x) / model.movementSpeed
            model.position.y += (model.destination.y - model.position.y) / model.movementSpeed
        }

    }

    fun attack() {
        model.hasHit = true
        die()
    }

    fun spawn() {
        view = BulletView(model)
    }
}
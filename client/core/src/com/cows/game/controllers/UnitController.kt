package com.cows.game.controllers

import com.badlogic.gdx.math.Vector2
import com.cows.game.map.Map
import com.cows.game.models.UnitModel
import com.cows.game.views.UnitView


class UnitController(val model: UnitModel): Updatable() {
    private lateinit var view: UnitView
    private var target = Vector2();
    var currentPathIndex = 0;
    var hasSpawned = false


    override fun update(deltaTime: Float) {
        if (!hasSpawned) return

        val velocity = model.currentDirection.toVector2()

        val deltaX = model.movementSpeed * velocity.x * deltaTime
        val deltaY = model.movementSpeed * velocity.y * deltaTime

        model.position.x += deltaX
        model.position.y += deltaY

        if (hasReachedTarget()) setNewTarget()
    }

    private fun hasReachedTarget(): Boolean {
        if (model.currentDirection.x < 0 && model.position.x < target.x) return true
        if (model.currentDirection.x > 0 && model.position.x > target.x) return true
        if (model.currentDirection.y < 0 && model.position.y < target.y) return true
        if (model.currentDirection.y > 0 && model.position.y > target.y) return true
        return false
    }

    private fun setNewTarget(){
        if (hasSpawned) model.position = target //ensure no overshooting

        if (currentPathIndex + 1 >= Map.PATH.size) {
            win() // this logic wont be here, should be called from event log
            return
        }

        val currentTarget = Map.PATH[currentPathIndex]
        currentPathIndex += 1
        val newTarget = Map.PATH[currentPathIndex]

        model.currentDirection = newTarget - currentTarget

        if (hasSpawned) view.updateSprite()

        target = newTarget.toVector2()
    }

    fun move(tile: TileController) {
        //removed as it caused movement to be buggy as there are competing calls in a single tick
        //target = tile.tileModel.coordinate.toVector2()
    }

    override fun die() {
        super.die()

        model.isDead = true;
    }

    fun win() {
        // TODO: Implement this
    }

    fun spawn(tile: TileController) {
        model.position = tile.tileModel.coordinate.toVector2()
        setNewTarget()
        view = UnitView(model)
        hasSpawned = true
    }
}
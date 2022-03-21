package com.cows.game.managers

import com.cows.game.controllers.Updatable

object Updater {
    private val updatables = mutableListOf<Updatable>()
    private val updatablesToBeAdded = mutableListOf<Updatable>()
    private val updatablesToBeRemoved = mutableListOf<Updatable>()


    fun update(deltaTime: Float) {
        updatables.forEach { it.update(deltaTime)}

        updatablesToBeAdded.forEach { updatables.add(it) }
        updatablesToBeAdded.clear()
        updatablesToBeRemoved.forEach { updatables.remove(it) }
        updatablesToBeRemoved.clear()
    }

    fun addUpdatable(updatable: Updatable) = updatablesToBeAdded.add(updatable)

    fun removeUpdatable(updatable: Updatable) = updatablesToBeRemoved.add(updatable)

}
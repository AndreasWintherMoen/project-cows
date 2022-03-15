package com.cows.game

import com.cows.game.controllers.Updatable
import com.cows.game.views.Renderable

object Updater {
    private val updatable: MutableList<Updatable> = mutableListOf<Updatable>()


    fun render(deltaTime: Float) {
        updatable.forEach { it.update(deltaTime)}
    }

    fun addUpdatable(updatable: Updatable) = Updater.updatable.add(updatable)

    // This function tries to remove an updatable from the updater, and prints a message, should the
    // updatable not be in the list of updatables.
    fun removeUpdatable(updatable: Updatable){
        if (!Updater.updatable.remove(updatable)){
            print("An attempt to remove ${updatable} was made, although it was not in the list of updatables. ")
            TODO("Implement proper logging for this case")
        }
    }

}
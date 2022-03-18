package com.cows.game.roundSimulation.simulationModels

import com.cows.game.Coordinate

class TowerSimulationModel(val id :Int, val position: Coordinate, val range : Int, private val timeBetweenAttacks : Int, private val damage : Int ) {
    var target : UnitSimulationModel? = null
    var cooldown : Int = 0

    fun decrementCooldown(){
        if(cooldown < 0){
            cooldown--
        }
    }

    fun setCooldown(){
        cooldown = timeBetweenAttacks
    }
    fun attack(){
        target!!.damage(damage)
    }


}


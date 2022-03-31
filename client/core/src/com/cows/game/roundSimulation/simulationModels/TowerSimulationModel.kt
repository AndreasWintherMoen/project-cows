package com.cows.game.roundSimulation.simulationModels


import com.cows.game.map.Coordinate
import com.cows.game.roundSimulation.ActionType
import com.cows.game.roundSimulation.rawJsonData.JsonAction

class TowerSimulationModel(val id :Int, val position: Coordinate, range : Int, unitPath: List<IntArray>, private val timeBetweenAttacks : Int, private val damage : Int ) {
    var target : UnitSimulationModel? = null
    var cooldown : Int = 0
    var pathIndicesInRange = findPathIndicesInRange(range, unitPath)

    fun decrementCooldown(){
        if(cooldown < 0){
            cooldown--
        }
    }

    fun setCooldown(){
        cooldown = timeBetweenAttacks
    }

    fun attack() {
        target!!.damage(damage)
    }

    fun targetInRange() : Boolean = target != null && pathIndicesInRange.contains(target!!.pathIndex)


    fun findPathIndicesInRange(range : Int, path : List<IntArray> ) : IntArray {
        // TODO implement, so that it only needs to check these positions
        return IntArray(0)
    }

    fun findNewTarget(): UnitSimulationModel? {
        // TODO implement this
        return null
    }

    fun findNewTarget(units : MutableList<UnitSimulationModel>): UnitSimulationModel? {
        //Assumption: pathIndicesInRange are ordered in decreasing order
        //room for optimization if needed, eg by checking if the unit pos is outside of the range, and then prune it away
        pathIndicesInRange.forEach { indice ->
            units.forEach{ unit ->
                if(unit.movementProgress == indice){
                    return unit
                }
            }
        }
        return null
    }


}


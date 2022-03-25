package com.cows.game.roundSimulation

import com.cows.game.map.Map
import com.cows.game.roundSimulation.rawJsonData.*
import com.cows.game.roundSimulation.simulationModels.TowerSimulationModel
import com.cows.game.roundSimulation.simulationModels.UnitSimulationModel
import kotlin.math.roundToInt

class RoundSimulator {

    private var gameOver = false
    private val eventLog = mutableListOf<JsonTick>();
    private val currentTick = arrayListOf<JsonAction>()


    //Input: attaclInstruction, defendInstruction og GameState
    //Output RoundSimulatoion
    //TODO add gameState, to simulate money, store map etc.
    fun simulate(map : Map, defendInstruction : List<JsonTower>, attackInstruction : List<JsonUnit>) : JsonRoundSimulation {

        val path = Map.getPathCoordinates()

        //init simulation
        val units = mutableListOf<UnitSimulationModel>()
        val towers = mutableListOf<TowerSimulationModel>()

        //TODO possibly split this up so they dont "spawn" at once
        //TODO add spawn instruction
        attackInstruction.forEach{units.add(UnitSimulationModel(it.id, 4, 0, 5, 0))}
        defendInstruction.forEach{towers.add(TowerSimulationModel(it.id, it.position, it.range.roundToInt(), path, 2, 2, ))}


        //perform simulation and populate eventlog
        while(!gameOver){
            currentTick.clear()
            currentTick += towers.mapNotNull { calculateTower(it) }
            currentTick += units.mapNotNull { calculateUnit(it, path) }
            eventLog.add(JsonTick(currentTick))
        }

        return JsonRoundSimulation(defendInstruction, attackInstruction, eventLog)
    }

    private fun calculateUnit(unit : UnitSimulationModel, path : List<IntArray> ): JsonAction? {
            if (unit.isDead()) return JsonAction(unit.id, ActionType.DIE, null)

            //if the unit is at goal position
            if (unit.pathIndex == path.size-1) {
                gameOver = true
                return JsonAction(unit.id, ActionType.WIN, null)
            }
            else{
                //need to handle unit movement here, unless we want to add the path to each individual unit.
                unit.incrementMovementProgress()
                if(unit.movementProgress >= unit.movementSpeed){
                    unit.move()
                    unit.movementProgress = 0
                    return JsonAction(unit.id, ActionType.MOVE, null)
                }
                return null
            }
        }

    private fun calculateTower(tower : TowerSimulationModel, units : MutableList<UnitSimulationModel>): JsonAction? {
        tower.decrementCooldown()

        // if no target, either set new target if a unit is in range, or do nothing if no unit in range
        if (tower.target == null) {
            val newTarget = tower.findNewTarget(units) ?: return null
            tower.target = newTarget
            return JsonAction(tower.id, ActionType.TARGET, tower.target!!.id)
        }

        // if target is no longer in range, target a new unit (if in range), or null (if none in range)
        if (!tower.targetInRange()) {
            val newTarget = tower.findNewTarget(units)
            tower.target = newTarget
            return JsonAction(tower.id, ActionType.TARGET, tower.target?.id)
        }

        // if cooldown is 0, attack target
        if (tower.cooldown == 0) {
            tower.attack()
            tower.setCooldown()
            return JsonAction(tower.id, ActionType.ATTACK, tower.target!!.id)
        }

        // has target, but tower is still on cooldown, so do nothing
        return null
    }
}
package com.cows.game.roundSimulation

import com.cows.game.map.Map
import com.cows.game.roundSimulation.rawJsonData.*
import com.cows.game.roundSimulation.simulationModels.TowerSimulationModel
import com.cows.game.roundSimulation.simulationModels.UnitSimulationModel
import kotlin.math.roundToInt

class RoundSimulator {

    //Input: attackInstruction, defendInstruction og GameState
    //Output RoundSimulation
    //TODO add gameState, to simulate money, store map etc.
    fun simulate(defendInstruction : List<JsonTower>, attackInstruction : List<JsonUnit>) : JsonRoundSimulation {
        val eventLog = mutableListOf<JsonTick>();

        val path = Map.getPathCoordinates()

        //init simulation
        val units = mutableListOf<UnitSimulationModel>()
        val towers = mutableListOf<TowerSimulationModel>()


        var timeToNextSpawn = 0
        attackInstruction.forEach{
            units.add(UnitSimulationModel(it.id, 1, 0, 2, 0, timeToNextSpawn))
            timeToNextSpawn += 2; //TODO(make these attributes based on various units stats)

        }
        defendInstruction.forEach{
            towers.add(
                    TowerSimulationModel(
                    it.id, it.position, 2, path, 0, 1, ))}

        var gameOver = false
        //perform simulation and populate eventlog
        while(!gameOver) {
            val currentTick = arrayListOf<JsonAction>()

            units.forEach { it.incrementMovementProgress() }
            val unitActions = units.mapNotNull { calculateUnit(it) }
            unitActions.forEach { it.processAction() }
            currentTick += unitActions.map { it.toJsonAction() }

            towers.forEach { it.decrementCooldown() }
            val towerActions = towers.mapNotNull { calculateTowerAction(it, units) }
            towerActions.forEach { it.processAction() }
            currentTick += towerActions.map { it.toJsonAction() }

            eventLog.add(JsonTick(currentTick.clone() as ArrayList<JsonAction>))

            if (unitActions.any { it.type == ActionType.WIN }) {
                win()
                gameOver = true
            }

            else if (units.all { it.isDead}) {
                lose()
                gameOver = true
            }


        }

        return JsonRoundSimulation(defendInstruction, attackInstruction, eventLog)
    }

    private fun win() {
        println("attacker won")
        //gameOver = true
        //TODO return who won in gamestate
    }

    private fun lose() {
        //gameOver = true
        println("defender won")
        // return defender as winner
    }

    private fun calculateUnit(unit : UnitSimulationModel): SimulationAction? {
        if(unit.isDead) return null
        if(!unit.isSpawned){
            unit.timeToSpawn--
            return if(unit.timeToSpawn > 0) null else SpawnSimulationAction(unit,0)
        }
        if (unit.health <= 0) return DieSimulationAction (unit)
        if (unit.pathIndex == Map.PATH.size-1) return WinSimulationAction(unit)
        if (unit.movementProgress >= unit.movementSpeed){
            unit.resetMovementProgress()
            return MoveSimulationAction(unit, unit.pathIndex+1)
        }
        return null
    }

    private fun calculateTowerAction(tower : TowerSimulationModel, units : MutableList<UnitSimulationModel>): SimulationAction? {
        // if no target, either set new target if a unit is in range, or do nothing if no unit in range
        if (tower.target == null) {
            val newTarget = tower.findNewTarget(units) ?: return null
            return TargetSimulationAction(tower, newTarget)
        }
        println(!tower.targetInRange())
        // if target is no longer in range, target a new unit (if in range), or null (if none in range)
        if (!tower.targetInRange() || tower.target!!.isDead) {
            val newTarget = tower.findNewTarget(units)
            return TargetSimulationAction(tower, newTarget)
        }

        // if cooldown is 0, attack target
        if (tower.cooldown <= 0) {
            return AttackSimulationAction(tower)
        }
        // has target, but tower is still on cooldown, so do nothing
        return null
    }
}
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

        var ticksBetweenSpawns = 2;
        val units = attackInstruction.mapIndexed{index, unit ->
            UnitSimulationModel(unit.id, 1, 1,  index*ticksBetweenSpawns)
        }
        //TODO reconsider how we convert tower range to ints, as it is treaded as ints here.
        val towers = defendInstruction.mapIndexed{ index, tower ->
            TowerSimulationModel(tower.id, tower.position, tower.range.roundToInt(), path, 0, 1, )}

        var gameOver = false
        //perform simulation and populate eventlog

        while(!gameOver) {
            val currentTick = arrayListOf<JsonAction>()

            //Will first call calculateUnit/towers, which will perfom internal logic such as cooldowns or determining IF the unit should move this tick.
            //This generates a list of actions for the eventlog. This list of actions is then iterated over, and the effects such as "death, movement/attack/target is enacted)
            val unitActions = units.mapNotNull { calculateUnit(it) }
            unitActions.forEach { it.processAction() }
            currentTick += unitActions.map { it.toJsonAction() }

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
        unit.incrementMovementProgress()
        if (unit.health <= 0) return DieSimulationAction (unit)
        if (unit.pathIndex == Map.PATH.size-1) return WinSimulationAction(unit)
        if (unit.movementProgress >= unit.movementSpeed){
            unit.resetMovementProgress()
            return MoveSimulationAction(unit, unit.pathIndex+1)
        }
        return null
    }


    private fun calculateTowerAction(tower : TowerSimulationModel, units : List<UnitSimulationModel>): SimulationAction? {
        if(tower.cooldown> 0){
            tower.decrementCooldown()
        }

        //if it has no target, attempt to find a new one. If no new target is found, it will not log it in the eventlog
        if (tower.target == null) {
            val newTarget = tower.findNewTarget(units) ?: return null
            return TargetSimulationAction(tower, newTarget)
        }
        //when the current target becomes obsolete (out of range/died)
        if (!tower.targetInRange() || tower.target!!.isDead) {
            val newTarget = tower.findNewTarget(units)
            return TargetSimulationAction(tower, newTarget)
        }

        if (tower.cooldown <= 0) {
            return AttackSimulationAction(tower)
        }

        // has target, but tower is still on cooldown, so do nothing
        return null
    }
}
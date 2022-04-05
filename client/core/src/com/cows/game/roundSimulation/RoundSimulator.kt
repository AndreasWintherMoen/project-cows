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


    //Input: attackInstruction, defendInstruction og GameState
    //Output RoundSimulatoion
    //TODO add gameState, to simulate money, store map etc.
    fun simulate(defendInstruction : List<JsonTower>, attackInstruction : List<JsonUnit>) : JsonRoundSimulation {

        val path = Map.getPathCoordinates()

        //init simulation
        val units = mutableListOf<UnitSimulationModel>()
        val towers = mutableListOf<TowerSimulationModel>()

        //TODO add spawn instruction
        var timeToNextSpawn = 0
        attackInstruction.forEach{
            timeToNextSpawn += 5; //TODO(make these attributes based on various units stats)
            units.add(UnitSimulationModel(it.id, 4, 0, 5, 0, timeToNextSpawn))}
        defendInstruction.forEach{towers.add(TowerSimulationModel(it.id, it.position, it.range.roundToInt(), path, 2, 2, ))}


        //perform simulation and populate eventlog
        while(!gameOver){
            towers.forEach { it.decrementCooldown() }
            val towerActions = towers.mapNotNull { calculateTowerAction(it, units) }
            towerActions.forEach { it.processAction() }
            currentTick += towerActions.map { it.toJsonAction() }

            units.forEach { it.incrementMovementProgress() }
            val unitActions = units.mapNotNull { calculateUnit(it) }
            unitActions.forEach { it.processAction() }
            currentTick += unitActions.map { it.toJsonAction() }

            eventLog.add(JsonTick(currentTick))
            currentTick.clear()

            if (unitActions.any { it.type == ActionType.WIN }) win()
            else if (unitActions.all { it.type == ActionType.DIE }) lose()
        }

        return JsonRoundSimulation(defendInstruction, attackInstruction, eventLog)
    }

    private fun win() {
        gameOver = true
        // return attacker as winner
    }

    private fun lose() {
        gameOver = true
        // return defender as winner
    }

    fun calculateUnit(unit : UnitSimulationModel): SimulationAction? {
        if (unit.isDead()) return DieSimulationAction (unit)
        if (unit.pathIndex == Map.PATH.size-1) return WinSimulationAction(unit)
        if (unit.movementProgress >= unit.movementSpeed){
            unit.resetMovementProgress()
            return MoveSimulationAction(unit, unit.pathIndex + 1)
        }
        return null
    }

    fun calculateTowerAction(tower : TowerSimulationModel, units : MutableList<UnitSimulationModel>): SimulationAction? {
        // if no target, either set new target if a unit is in range, or do nothing if no unit in range
        if (tower.target == null) {
            val newTarget = tower.findNewTarget(units) ?: return EmptySimulationAction()
            return TargetSimulationAction(tower, newTarget)
        }

        // if target is no longer in range, target a new unit (if in range), or null (if none in range)
        if (!tower.targetInRange()) {
            val newTarget = tower.findNewTarget(units)
            return TargetSimulationAction(tower, newTarget)
        }

        // if cooldown is 0, attack target
        if (tower.cooldown == 0) {
            return AttackSimulationAction(tower)
        }

        // has target, but tower is still on cooldown, so do nothing
        return null
    }
}
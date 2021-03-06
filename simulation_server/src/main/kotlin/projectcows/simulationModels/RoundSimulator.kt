package projectcows.simulationModels

import projectcows.enums.ActionType
import projectcows.rawJsonData.*
import kotlin.math.roundToInt

class RoundSimulator {
    //Input: attackInstruction, defendInstruction og GameState
    //Output RoundSimulation
    //TODO add gameState, to simulate money, store map etc.
    fun simulate(defendInstruction : List<JsonTower>, attackInstruction : List<JsonUnit>, path: List<IntArray>) : JsonRoundSimulation {
        val eventLog = mutableListOf<JsonTick>();

        val attackInstructionsWithId = attackInstruction.mapIndexed {index, unit -> UnitStatsMapper.appendMissingDataToJsonUnit(index, unit)}
        val defendInstructionsWithId = defendInstruction.mapIndexed {index, tower -> UnitStatsMapper.appendMissingDataToJsonTower(index, tower)}

        val units = attackInstruction.mapIndexed{index, unit -> UnitStatsMapper.jsonUnitToSimulationModel(index, unit) }
        //TODO reconsider how we convert tower range to ints, as it is treaded as ints here.
        val towers = defendInstruction.mapIndexed{ index, tower -> UnitStatsMapper.jsonTowerToSimulationModel(index, tower, path) }

        var gameOver = false
        var attackerWon = false
        //perform simulation and populate eventlog

        while(!gameOver) {
            val currentTick = arrayListOf<JsonAction>()

            //Will first call calculateUnit/towers, which will perfom internal logic such as cooldowns or determining IF the unit should move this tick.
            //This generates a list of actions for the eventlog. This list of actions is then iterated over, and the effects such as "death, movement/attack/target is enacted)
            val unitActions = units.mapNotNull { calculateUnit(it, path.size) }
            unitActions.forEach { it.processAction() }
            currentTick += unitActions.map { it.toJsonAction() }

            val towerActions = towers.mapNotNull { calculateTowerAction(it, units) }
            towerActions.forEach { it.processAction() }
            currentTick += towerActions.map { it.toJsonAction() }

            eventLog.add(JsonTick(currentTick.clone() as ArrayList<JsonAction>))

            if (unitActions.any { it.type == ActionType.WIN }) {
                attackerWon = true
                gameOver = true
            }

            else if (units.all { it.isDead}) {
                attackerWon = false
                gameOver = true
            }
        }
        return JsonRoundSimulation(defendInstructionsWithId, attackInstructionsWithId, eventLog, attackerWon)
    }

    private fun calculateUnit(unit : UnitSimulationModel, pathSize: Int): SimulationAction? {
        if(unit.isDead) return null
        if(!unit.isSpawned){
            unit.timeToSpawn--
            return if(unit.timeToSpawn > 0) null else SpawnSimulationAction(unit,0)
        }
        unit.incrementMovementProgress()
        if (unit.health <= 0) return DieSimulationAction (unit)
        if (unit.pathIndex == pathSize-1) return WinSimulationAction(unit)
        if (unit.movementProgress >= 50){
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
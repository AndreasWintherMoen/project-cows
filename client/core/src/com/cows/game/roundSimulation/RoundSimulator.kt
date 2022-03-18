package com.cows.game.roundSimulation

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
    //TODO add gameState, to simulate money etc.
    //TODO need to know the path (and start + end position)
    fun simulate( defendInstruction : List<JsonTower>, attackInstruction : List<JsonUnit>) : JsonRoundSimulation {

        //init simulation
        var units = mutableListOf<UnitSimulationModel>()
        val towers = mutableListOf<TowerSimulationModel>()

        //TODO add cordinates
        //TODO possibly split this up so they dont "spawn" at once
        //TODO add spawn instruction
        //TODO implement
        attackInstruction.forEach{units.add(UnitSimulationModel(it.id, 4, 0, 5))}
        defendInstruction.forEach{towers.add(TowerSimulationModel(it.id, it.position, it.range.roundToInt(), 2, 1))}


        //perform simulation and populate eventlog
        while(!gameOver){
            currentTick.clear()
            units.forEach{calculateUnit(it)}
            towers.forEach{calculateTower(it)}
            eventLog.add(JsonTick(currentTick))
        }

        return JsonRoundSimulation(defendInstruction, attackInstruction, eventLog)
    }

    fun calculateUnit(unit : UnitSimulationModel){
        //todo find goal position
            if (unit.position == goalPos) {
                gameOver = true
                currentTick.add(JsonAction(unit.id, ActionType.WIN, null))
            }
            else{
                //need to handle unit movement here, unless we want to add the path to each individual unit.
                unit.incrementMovementProgress()
                if(unit.movementProgress >= unit.movementSpeed){
                    //unit.move() //TODO get function to find next place to move to
                    currentTick.add(JsonAction(unit.id, ActionType.MOVE, null))

                }

            }
        }

    fun calculateTower(tower : TowerSimulationModel){
            if (tower.target != null) {
                if(tower.targetInRange()){ //TODO implement
                    if(tower.cooldown == 0){
                        //simulates an attack
                        //check here if unit is attacked tower.target.health-turretdamage; unit.Health <= 0 ? unit.kill()
                        tower.attack()
                        currentTick.add(JsonAction(tower.id, ActionType.ATTACK, tower.target!!.id))
                        tower.setCooldown()

                        if (tower.target!!.isDead()){
                            currentTick.add(JsonAction(tower.target!!.id, ActionType.DIE, null))
                            //tower.updateTarget() TODO implement

                            if(tower.target != null){
                                currentTick.add(JsonAction(tower.id, ActionType.TARGET, tower.target!!.id))
                            }
                        }
                    }else{
                        tower.decrementCooldown()
                    }
                }else{
                    //tower.updateTarget() //TODO repetetive code, consider changing this
                    if(tower.target != null){
                        currentTick.add(JsonAction(tower.id, ActionType.TARGET, tower.target!!.id))
                    }
                }
            } else{
                //tower.updateTarget() //TODO implement
                if(tower.target != null){
                    currentTick.add(JsonAction(tower.id, ActionType.TARGET, tower.target!!.id))
                }
            }
    }
}
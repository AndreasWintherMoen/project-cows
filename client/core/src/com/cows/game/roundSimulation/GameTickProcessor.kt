package com.cows.game.roundSimulation

import com.cows.game.map.Map
import com.cows.game.controllers.TowerController
import com.cows.game.controllers.UnitController
import com.cows.game.roundSimulation.rawJsonData.JsonAction
import com.cows.game.roundSimulation.rawJsonData.JsonRoundSimulation
import com.cows.game.roundSimulation.rawJsonData.JsonTick
import java.util.*
import kotlin.collections.ArrayList

class GameTickProcessor (private val roundSimulation: JsonRoundSimulation, private val onFinishGame: () -> Unit) {
    private var tickTimer = 0f
    private val towerList = hashMapOf<Int, TowerController>()
    private val unitList = hashMapOf<Int, UnitController>()
    private val eventLog: Queue<Tick> = LinkedList()
    private var gameIsFinished = false

    init {
        roundSimulation.towerList.forEach { towerList[it.id!!] = TowerController(it.toTowerModel()) }
        roundSimulation.unitList.forEach { unitList[it.id!!] = UnitController(it.toUnitModel()) }
        roundSimulation.eventLog.map { concretizeTick(it) }.forEach { eventLog.add(it) }
    }

    fun update(deltaTime: Float, tickDuration: Float) {
        if (gameIsFinished) return


        tickTimer += deltaTime
        if (tickTimer >= tickDuration) {
            tickTimer -= tickDuration
            if (eventLog.size == 0) {
                println("empty event log")
                gameIsFinished = true
                onFinishGame.invoke()
                return
            }
            val tick = eventLog.remove()
//            println("processing tick $tick")
            processTick(tick)
        }
    }

    private fun processTick(tick: Tick) = tick.actions.forEach { it.processAction() }

    private fun concretizeTick(jsonTick: JsonTick): Tick = Tick(jsonTick.actions.map { concretizeAction(it) } as ArrayList<Action>)

    private fun concretizeAction(jsonAction: JsonAction): Action {
        return try {
            when (jsonAction.verb) {
                ActionType.TARGET -> TargetAction(towerList[jsonAction.subject]!!, unitList[jsonAction.obj])
                ActionType.ATTACK -> AttackAction(towerList[jsonAction.subject]!!)
                ActionType.MOVE -> MoveAction(unitList[jsonAction.subject]!!, Map.getTileAtPathIndex(jsonAction.obj!!))
                ActionType.DIE -> DieAction(unitList[jsonAction.subject]!!)
                ActionType.WIN -> WinAction(unitList[jsonAction.subject]!!)
                ActionType.SPAWN -> SpawnAction(unitList[jsonAction.subject]!!, Map.getTileAtPathIndex(jsonAction.obj!!))
                else -> throw Error("Could not find action type ${jsonAction.verb} on jsonAction $jsonAction")
            }
        } catch (e: Error) {
            println("Could not concretize action $jsonAction. Returning empty action. Error message: $e")
            EmptyAction()
        }
    }

    fun killAllUnits() {
        println("GameTickProcessor::killAllUnits")
        towerList.forEach { it.value.die() }
        unitList.forEach { it.value.die() }
    }

}
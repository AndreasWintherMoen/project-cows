package com.cows.game.roundSimulation

import com.cows.game.controllers.TowerController
import com.cows.game.controllers.UnitController
import java.util.*
import kotlin.collections.ArrayList

class GameLoopSimulator (private val roundSimulation: RoundSimulation) {
    private val TIME_BETWEEN_TICKS = 0.25f
    private var timer = 0f
    private val towerList = arrayListOf<TowerController>()
    private val unitList = arrayListOf<UnitController>()
    private val eventLog: Queue<Tick> = LinkedList()

    init {
        roundSimulation.towerList.forEach { towerList.add(TowerController(it)) }
        roundSimulation.unitList.forEach { unitList.add(UnitController(it)) }
        roundSimulation.eventLog.forEach { eventLog.add(it) }
    }

    fun update(deltaTime: Float) {
        timer += deltaTime
        if (timer >= TIME_BETWEEN_TICKS) {
            timer -= TIME_BETWEEN_TICKS
            // process next tick
            val tick = eventLog.remove()
            processTick(tick)
        }
    }

    fun processTick(tick: Tick) = tick.actions.forEach { processAction(it) }

    fun processAction(action: Action) {
        println("Processing action $action")
//        if (action.)
////        action.subject
////        val tower = towerList[action.subject]
//        when (action.verb) {
//            ActionTypes. -> tower.move
//        }
    }

}
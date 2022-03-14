package com.cows.game.roundSimulation

import com.cows.game.controllers.TowerController
import com.cows.game.controllers.UnitController
import java.util.*

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
            if (eventLog.size == 0) {
                println("empty event log")
                return
            }
            val tick = eventLog.remove()
            processTick(tick)
        }
    }

    fun processTick(tick: Tick) {
        if (tick.actions.size == 0) return
        tick.actions.forEach { processAction(it) }
    }

    fun processAction(action: Action) {
        println("Processing action $action")
    }

}
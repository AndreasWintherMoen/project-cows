package com.cows.game

import com.cows.game.controllers.TowerController
import com.cows.game.controllers.UnitController
import com.cows.game.roundSimulation.Action
import com.cows.game.roundSimulation.RoundSimulation

class GameLoopSimulator (val roundSimulation: RoundSimulation) {
    private val TIME_BETWEEN_TICKS = 0.25f
    private lateinit var towerList: ArrayList<TowerController>
    private lateinit var unitList: ArrayList<UnitController>
    private lateinit var eventLog: ArrayList<Action>
    private var timer = 0f

    init {
        roundSimulation.towerList.forEach { towerList.add(TowerController(it)) }
        roundSimulation.unitList.forEach { unitList.add(UnitController(it)) }
        roundSimulation.eventLog.forEach { eventLog.add(it) }
    }

    fun update(deltaTime: Float) {
        timer += deltaTime

    }
}
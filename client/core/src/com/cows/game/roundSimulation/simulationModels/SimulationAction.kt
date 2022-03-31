package com.cows.game.roundSimulation

import com.cows.game.controllers.TileController
import com.cows.game.roundSimulation.rawJsonData.JsonAction
import com.cows.game.roundSimulation.simulationModels.TowerSimulationModel
import com.cows.game.roundSimulation.simulationModels.UnitSimulationModel

abstract class SimulationAction {
    abstract fun processAction()
    abstract val type: ActionType
    abstract fun toJsonAction(): JsonAction
}

class EmptySimulationAction(): SimulationAction() {
    override fun processAction() {}
    override val type = ActionType.NONE
    override fun toJsonAction(): JsonAction {
        TODO("Not yet implemented")
    }
}

data class TargetSimulationAction(val tower: TowerSimulationModel, val unit: UnitSimulationModel?): SimulationAction() {
    override fun processAction() {
        if (unit == null) tower.target = null
        else tower.target = unit
    }
    override val type = ActionType.TARGET
    override fun toJsonAction(): JsonAction {
        TODO("Not yet implemented")
    }

}

data class AttackSimulationAction(val tower: TowerSimulationModel): SimulationAction() {
    override fun processAction() = tower.attack()
    override val type = ActionType.ATTACK
    override fun toJsonAction(): JsonAction {
        TODO("Not yet implemented")
    }
}

data class MoveSimulationAction(val unit: UnitSimulationModel, val tileIndex: Int): SimulationAction() {
    override fun processAction() = unit.move()
    override val type = ActionType.MOVE
    override fun toJsonAction(): JsonAction {
        TODO("Not yet implemented")
    }
}

data class DieSimulationAction(val unit: UnitSimulationModel): SimulationAction() {
    override fun processAction() = unit.die()
    override val type = ActionType.DIE
    override fun toJsonAction(): JsonAction {
        TODO("Not yet implemented")
    }
}

data class WinSimulationAction(val unit: UnitSimulationModel): SimulationAction() {
    override fun processAction() = unit.win()
    override val type = ActionType.WIN
    override fun toJsonAction(): JsonAction {
        TODO("Not yet implemented")
    }
}

data class SpawnSimulationAction(val unit: UnitSimulationModel, val tile: TileController): SimulationAction() {
    override fun processAction() {} //unit.spawn(tile)
    override val type = ActionType.SPAWN
    override fun toJsonAction(): JsonAction {
        TODO("Not yet implemented")
    }
}

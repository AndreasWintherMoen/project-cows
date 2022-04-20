package com.cows.game.roundSimulation

import com.badlogic.gdx.utils.Json
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
        return JsonAction(1, type, null)
    }
}

data class TargetSimulationAction(val tower: TowerSimulationModel, val unit: UnitSimulationModel?): SimulationAction() {
    override fun processAction() {
        if (unit == null) tower.target = null
        else tower.target = unit
    }
    override val type = ActionType.TARGET
    override fun toJsonAction(): JsonAction {
        return JsonAction(tower.id, type, unit?.id)
    }

}

data class AttackSimulationAction(val tower: TowerSimulationModel): SimulationAction() {
    override fun processAction() = tower.attack()
    override val type = ActionType.ATTACK
    override fun toJsonAction(): JsonAction {
        return JsonAction(tower.id, type, null)
    }
}

data class MoveSimulationAction(val unit: UnitSimulationModel, val tileIndex: Int): SimulationAction() {
    override fun processAction() = unit.move()
    override val type = ActionType.MOVE
    override fun toJsonAction(): JsonAction {
        //TODO("check if this is a necessary action")
        return JsonAction(unit.id, type, tileIndex)
    }
}

data class DieSimulationAction(val unit: UnitSimulationModel): SimulationAction() {
    override fun processAction() = unit.die()
    override val type = ActionType.DIE
    override fun toJsonAction(): JsonAction {
        return JsonAction(unit.id, type, null)
    }
}

data class WinSimulationAction(val unit: UnitSimulationModel): SimulationAction() {
    override fun processAction() = unit.win()
    override val type = ActionType.WIN
    override fun toJsonAction(): JsonAction {
        return JsonAction(unit.id, type, null)
    }
}

data class SpawnSimulationAction(val unit: UnitSimulationModel, val pathIndex : Int): SimulationAction() {
    override fun processAction() = unit.spawn()
    override val type = ActionType.SPAWN
    override fun toJsonAction(): JsonAction {
        //TODO check if we should do something with regards to type here
        return JsonAction(unit.id, type, pathIndex)
    }
}

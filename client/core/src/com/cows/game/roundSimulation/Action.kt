package com.cows.game.roundSimulation

import com.cows.game.controllers.TileController
import com.cows.game.controllers.TowerController
import com.cows.game.controllers.UnitController

enum class ActionType {
    NONE,
    TARGET,
    ATTACK,
    MOVE,
    DIE,
    WIN,
    SPAWN
}

abstract class Action {
    abstract fun processAction()
    abstract val type: ActionType
}

class EmptyAction(): Action() {
    override fun processAction() {}
    override val type = ActionType.NONE
}

data class TargetAction(val tower: TowerController, val unit: UnitController?): Action() {
    override fun processAction() {
        if (unit == null) tower.removeTarget()
        else tower.target(unit)
    }
    override val type = ActionType.TARGET

}

data class AttackAction(val tower: TowerController): Action() {
    override fun processAction() = tower.attack()
    override val type = ActionType.ATTACK
}

data class MoveAction(val unit: UnitController, val tile: TileController): Action() {
    override fun processAction() = unit.move(tile)
    override val type = ActionType.MOVE
}

data class DieAction(val unit: UnitController): Action() {
    override fun processAction() = unit.die()
    override val type = ActionType.DIE
}

data class WinAction(val unit: UnitController): Action() {
    override fun processAction() = unit.win()
    override val type = ActionType.WIN
}

data class SpawnAction(val unit: UnitController, val tile: TileController): Action() {
    override fun processAction() = unit.spawn(tile)
    override val type = ActionType.SPAWN
}
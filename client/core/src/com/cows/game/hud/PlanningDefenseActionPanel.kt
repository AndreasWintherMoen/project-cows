package com.cows.game.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.cows.game.enums.UnitType
import com.cows.game.managers.TowerSpawner

class PlanningDefenseActionPanel(private val onStartGame: () -> Unit): PlanningActionPanel() {
    val cancelPlacementButton = Button("HUD/cancel-button.png", Vector2(Gdx.graphics.width - 100f, 30f)) { cancelPlacement() }


    init {
        cancelPlacementButton.hide = true
        fireTowerButton.onClick =  { selectTower(UnitType.FIRE)}
        waterTowerButton.onClick = { selectTower(UnitType.WATER) }
        grassTowerButton.onClick = { selectTower(UnitType.GRASS) }
        startGameButton.onClick = { onStartGame.invoke() }

    }

    fun cancelPlacement() {
        TowerSpawner.cancelPlacement()
        cancelPlacementButton.hide = true
    }

    fun confirmPlacement() {
        TowerSpawner.cancelPlacement()
        cancelPlacementButton.hide = true
    }

    fun selectTower(type: UnitType) {
        if (unitCounterPanel.hasAvailableUnits()) {
            TowerSpawner.selectTower(type) { confirmPlacement(); unitCounterPanel.addUnit(type) }
            cancelPlacementButton.hide = false
        } else {
            //TODO: Give the user some feedback that they don't have any units left
        }
    }

    override fun die() {
        super.die()
        cancelPlacementButton.die()
    }
}
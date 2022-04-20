package com.cows.game.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.cows.game.enums.TowerType
import com.cows.game.enums.UnitType
import com.cows.game.managers.FunctionDelayer
import com.cows.game.managers.TowerSpawner

class PlanningDefenseActionPanel(private val onStartGame: () -> Unit): PlanningActionPanel() {
    //val panelBatch = SpriteBatch()
    //private val buttons = mutableListOf<Button>()
    private val unitCounterPanel = UnitCounterPanel(10)
    val cancelPlacementButton = Button("HUD/cancel-button.png", Vector2(Gdx.graphics.width - 100f, 30f)) { cancelPlacement() }

    init {
        cancelPlacementButton.hide = true
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
        unitCounterPanel.die()
        startGameButton.die()
        grassTowerButton.die()
        waterTowerButton.die()
    }

    val startGameButton = Button("Buttons/start-button.png", Vector2(this.position.x+45f, 0f)) { println("STARTING GAME") }

    // FIRE TOWER🔥
    val fireTowerButton = Button("Towers/fire-tower.png", Vector2(this.position.x + 45f, 100f)) { selectTower(UnitType.FIRE) }

    // GRASS TOWER🌿
    val grassTowerButton = Button("Towers/grass-tower.png", Vector2(this.position.x + 45f, 200f)) { selectTower(UnitType.GRASS) }

    // WATER TOWER💧
    val waterTowerButton = Button("Towers/water-tower.png", Vector2(this.position.x + 45f, 300f)) { selectTower(UnitType.WATER) }
}
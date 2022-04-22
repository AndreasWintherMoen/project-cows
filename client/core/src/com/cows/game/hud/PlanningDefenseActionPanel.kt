package com.cows.game.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.cows.game.enums.UnitType
import com.cows.game.managers.TowerSpawner

class PlanningDefenseActionPanel(private val onStartGame: () -> Unit): PlanningActionPanel() {
    val cancelPlacementButton = Button("HUD/cancel-button.png", Vector2(Gdx.graphics.width - 100f, 30f)) { cancelPlacement() }

    val startGameButton = Button("Buttons/start-button.png", Vector2(this.position.x + 50f, 0f)) { onStartGame.invoke() }

    // FIRE TOWER🔥
    val fireTowerButton = Button("Towers/charizard-panel.png", Vector2(this.position.x, ACTION_HEIGHT - 109f- UNIT_MARGIN*0)) { selectTower(UnitType.FIRE) }

    // GRASS TOWER🌿
    val waterTowerButton = Button("Towers/blastoise-panel.png", Vector2(this.position.x , ACTION_HEIGHT - 109f*2f - UNIT_MARGIN*1)) { selectTower(UnitType.WATER) }

    // WATER TOWER💧
    val grassTowerButton = Button("Towers/venosaur-panel.png", Vector2(this.position.x, ACTION_HEIGHT - 109f*3f- UNIT_MARGIN*2)) { selectTower(UnitType.GRASS) }

    private val unitCounterPanel = UnitCounterPanel(10)

    init {
        cancelPlacementButton.hide = true
        fireTowerButton.position.x += ActionPanel.WIDTH/2 - fireTowerButton.texture.width/2
        waterTowerButton.position.x += ActionPanel.WIDTH/2 - waterTowerButton.texture.width/2
        grassTowerButton.position.x += ActionPanel.WIDTH/2 - grassTowerButton.texture.width/2
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
        fireTowerButton.die()
        grassTowerButton.die()
        waterTowerButton.die()
        cancelPlacementButton.die()
    }
}
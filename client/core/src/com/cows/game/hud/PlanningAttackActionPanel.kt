package com.cows.game.hud

import com.badlogic.gdx.math.Vector2
import com.cows.game.enums.UnitType

class PlanningAttackActionPanel(private val onStartGame: () -> Unit) : PlanningActionPanel() {

    init {
        fireTowerButton.disabled = true
        waterTowerButton.disabled = true
        grassTowerButton.disabled = true
        startGameButton.onClick = { onStartGame.invoke() }

    }

    // FIRE TOWER🔥
    val removeFireTowerButton = Button("Buttons/remove-button.png", Vector2(this.position.x+ 15f, 350f)) { unitCounterPanel.removeUnit(UnitType.FIRE)}
    val addFireTowerButton = Button("Buttons/add-button.png", Vector2(this.position.x + 150f, 350f)) { unitCounterPanel.addUnit(UnitType.FIRE)}

    // WATER TOWER💧
    val removeWaterTowerButton = Button("Buttons/remove-button.png", Vector2(this.position.x + 15f, 250f)) { unitCounterPanel.removeUnit(UnitType.WATER)}
    val addWaterTowerButton = Button("Buttons/add-button.png", Vector2(this.position.x + 150f, 250f)) { unitCounterPanel.addUnit(UnitType.WATER)}

    // GRASS TOWER🌿
    val removeGrassTowerButton = Button("Buttons/remove-button.png", Vector2(this.position.x+ 15f, 150f)) { unitCounterPanel.removeUnit(UnitType.GRASS)}
    val addGrassTowerButton = Button("Buttons/add-button.png", Vector2(this.position.x + 150f, 150f)) { unitCounterPanel.addUnit(UnitType.GRASS)}


    override fun die() {
        super.die()

        removeFireTowerButton.die()
        addFireTowerButton.die()

        removeWaterTowerButton.die()
        addWaterTowerButton.die()

        removeGrassTowerButton.die()
        addGrassTowerButton.die()
    }
}

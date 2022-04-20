package com.cows.game.hud

import com.badlogic.gdx.math.Vector2
import com.cows.game.enums.UnitType

class PlanningAttackActionPanel(private val onStartGame: () -> Unit) : PlanningActionPanel() {
    private val unitCounterPanel = UnitCounterPanel(10)

    val startGameButton = Button("Buttons/start-button.png", Vector2(this.position.x+45f, 0f)) { onStartGame.invoke() }

    // FIRE TOWER🔥
    val fireTowerButton = Button("Towers/fire-tower.png", Vector2(this.position.x + 45f, 100f)) { println("OPEN FIRE TOWER INFO")}
    val removeFireTowerButton = Button("Buttons/remove-button.png", Vector2(this.position.x, 100f)) { unitCounterPanel.removeUnit(UnitType.FIRE)}
    val addFireTowerButton = Button("Buttons/add-button.png", Vector2(this.position.x+ 150f, 100f)) { unitCounterPanel.addUnit(UnitType.FIRE)}


    // GRASS TOWER🌿
    val grassTowerButton = Button("Towers/grass-tower.png", Vector2(this.position.x + 45f, 200f)) { println("OPEN GRASS TOWER INFO")}
    val removeGrassTowerButton = Button("Buttons/remove-button.png", Vector2(this.position.x, 200f)) { unitCounterPanel.removeUnit(UnitType.GRASS)}
    val addGrassTowerButton = Button("Buttons/add-button.png", Vector2(this.position.x+ 150f, 200f)) { unitCounterPanel.addUnit(UnitType.GRASS)}

    // WATER TOWER💧
    val waterTowerButton = Button("Towers/water-tower.png", Vector2(this.position.x + 45f, 300f)) { println("OPEN WATER TOWER INFO")}
    val removeWaterTowerButton = Button("Buttons/remove-button.png", Vector2(this.position.x, 300f)) { unitCounterPanel.removeUnit(
        UnitType.WATER)}
    val addWaterTowerButton = Button("Buttons/add-button.png", Vector2(this.position.x+ 150f, 300f)) { unitCounterPanel.addUnit(
        UnitType.WATER)}

    override fun die() {
        super.die()

        startGameButton.die()

        fireTowerButton.die()
        removeFireTowerButton.die()
        addFireTowerButton.die()

        waterTowerButton.die()
        removeWaterTowerButton.die()
        addWaterTowerButton.die()

        grassTowerButton.die()
        removeGrassTowerButton.die()
        addGrassTowerButton.die()
    }
}

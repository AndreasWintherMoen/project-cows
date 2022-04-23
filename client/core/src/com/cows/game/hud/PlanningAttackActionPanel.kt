package com.cows.game.hud

import com.badlogic.gdx.math.Vector2
import com.cows.game.Redux
import com.cows.game.enums.UnitType
import com.cows.game.serverConnection.ServerConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlanningAttackActionPanel() : PlanningActionPanel() {

    init {
        fireTowerButton.disabled = true
        waterTowerButton.disabled = true
        grassTowerButton.disabled = true
//        startGameButton.onClick = { onStartGame.invoke() }
        startGameButton.onClick = { onStartButtonClicked() }
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

    private fun onStartButtonClicked() {
        println("PlanningAttackActionPanel::onStartButtonClicked")
        val units = unitCounterPanel.getJsonUnitList()
        GlobalScope.launch(Dispatchers.IO) {
            val roundSimulation = ServerConnection.sendAttackInstructions(units)
            println("Received sound simulation $roundSimulation")
            Redux.jsonRoundSimulation = roundSimulation
        }
    }


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

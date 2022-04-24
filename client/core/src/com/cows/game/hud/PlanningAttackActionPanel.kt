package com.cows.game.hud

import com.badlogic.gdx.math.Vector2
import com.cows.game.Redux
import com.cows.game.enums.UnitType
import com.cows.game.serverConnection.ServerConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlanningAttackActionPanel() : PlanningActionPanel() {
    // FIRE TOWER🔥
    private val fireTowerData = Redux.jsonAvailableTowers!!.getTower(UnitType.FIRE)
    val fireTowerBackground = SmartObject("HUD/banner-fire.png", Vector2(this.position.x+32f, 323f), 1f)
    val fireDamageNumber = FontObject(fireTowerData.damage.toString(), 25, Vector2(this.position.x + 70f, 439f))
    val fireRangeNumber = FontObject(fireTowerData.damage.toString(), 25, Vector2(this.position.x + 135f, 439f))
    val fireTowerButton = Button("Cards/"+getUnitName(UnitType.FIRE, fireTowerData.level)+".png", Vector2(this.position.x, 330f))

    // GRASS TOWER🌿
    private val grassTowerData = Redux.jsonAvailableTowers!!.getTower(UnitType.GRASS)
    val grassTowerBackground = SmartObject("HUD/banner-grass.png", Vector2(this.position.x+32f , 199f), 1f)
    val grassDamageNumber = FontObject(grassTowerData.damage.toString(), 25, Vector2(this.position.x + 70f, 315f))
    val grassRangeNumber = FontObject(grassTowerData.damage.toString(), 25, Vector2(this.position.x + 135f, 315f))
    val waterTowerButton = Button("Cards/"+getUnitName(UnitType.GRASS, grassTowerData.level)+".png", Vector2(this.position.x , 206f))

    // WATER TOWER💧
    private val waterTowerData = Redux.jsonAvailableTowers!!.getTower(UnitType.WATER)
    val waterTowerBackground = SmartObject("HUD/banner-water.png", Vector2(this.position.x+32f, 75f), 1f)
    val waterDamageNumber = FontObject(waterTowerData.damage.toString(), 25, Vector2(this.position.x + 70f, 192f))
    val waterRangeNumber = FontObject(waterTowerData.damage.toString(), 25, Vector2(this.position.x + 135f, 192f))
    val grassTowerButton = Button("Cards/"+getUnitName(UnitType.WATER, waterTowerData.level)+".png", Vector2(this.position.x, 83f))


    init {
        fireTowerButton.disabled = true
        waterTowerButton.disabled = true
        grassTowerButton.disabled = true
//        startGameButton.onClick = { onStartGame.invoke() }
        readyButton.onClick = { onStartButtonClicked() }
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

    val unitCounterPanel = UnitCounterPanel(10)


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

        unitCounterPanel.die()

        removeFireTowerButton.die()
        addFireTowerButton.die()

        removeWaterTowerButton.die()
        addWaterTowerButton.die()

        removeGrassTowerButton.die()
        addGrassTowerButton.die()
        fireTowerButton.die()
        grassTowerButton.die()
        waterTowerButton.die()
        fireTowerBackground.die()
        grassTowerBackground.die()
        waterTowerBackground.die()
        fireDamageNumber.die()
        fireRangeNumber.die()
        grassDamageNumber.die()
        grassRangeNumber.die()
        waterDamageNumber.die()
        waterRangeNumber.die()
    }

    override fun hideUI(hide: Boolean) {
        TODO("Not yet implemented")
    }
}

package com.cows.game.hud

import com.badlogic.gdx.math.Vector2
import com.cows.game.Redux
import com.cows.game.enums.UnitType
import com.cows.game.managers.RoundManager
import com.cows.game.serverConnection.ServerConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlanningAttackActionPanel() : PlanningActionPanel() {
    val unitCounterPanel = UnitCounterPanel()

    private val peekPanel = PeekPanel()

    // FIRE UNIT🔥
    private val fireUnitData = RoundManager.gameStatus!!.availableUnits.fireUnit
    val fireUnitBackground = SmartObject("Cards/banner-fire-attack.png", Vector2(this.position.x+32f, 323f), 1f)
    val fireHealthNumber = FontObject(fireUnitData.health.toString(), 25, Vector2(this.position.x + 70f, 439f))
    val fireMovementSpeedNumber = FontObject(fireUnitData.movementSpeed.toString(), 25, Vector2(this.position.x + 135f, 439f))
    val removeFireUnitButton = Button("Buttons/remove-button.png", Vector2(this.position.x+ 12f, 361f)) { unitCounterPanel.removeUnit(UnitType.FIRE)}
    val addFireUnitButton = Button("Buttons/add-button.png", Vector2(this.position.x + 157f, 361f)) { unitCounterPanel.addUnit(UnitType.FIRE)}
    val fireUnitButton = Button("Cards/"+getUnitName(UnitType.FIRE, fireUnitData.level)+".png", Vector2(this.position.x , 330f))

    // GRASS UNIT🌿
    private val grassUnitData = RoundManager.gameStatus!!.availableUnits.grassUnit
    val grassUnitBackground = SmartObject("Cards/banner-grass-attack.png", Vector2(this.position.x+32f , 199f), 1f)
    val grassHealthNumber = FontObject(grassUnitData.health.toString(), 25, Vector2(this.position.x + 70f, 315f))
    val grassMovementSpeedNumber = FontObject(grassUnitData.movementSpeed.toString(), 25, Vector2(this.position.x + 135f, 315f))
    val removeGrassUnitButton = Button("Buttons/remove-button.png", Vector2(this.position.x+ 12f, 236f)) { unitCounterPanel.removeUnit(UnitType.GRASS)}
    val addGrassUnitButton = Button("Buttons/add-button.png", Vector2(this.position.x + 157f, 236f)) { unitCounterPanel.addUnit(UnitType.GRASS)}
    val grassUnitButton = Button("Cards/"+getUnitName(UnitType.GRASS, grassUnitData.level)+".png", Vector2(this.position.x, 206f))

    // WATER UNIT💧
    private val waterUnitData = RoundManager.gameStatus!!.availableUnits.waterUnit
    val waterUnitBackground = SmartObject("Cards/banner-water-attack.png", Vector2(this.position.x+32f, 75f), 1f)
    val waterHealthNumber = FontObject(waterUnitData.health.toString(), 25, Vector2(this.position.x + 70f, 192f))
    val waterMovementSpeedNumber = FontObject(waterUnitData.movementSpeed.toString(), 25, Vector2(this.position.x + 135f, 192f))
    val removeWaterUnitButton = Button("Buttons/remove-button.png", Vector2(this.position.x + 12f, 111f)) { unitCounterPanel.removeUnit(UnitType.WATER)}
    val addWaterUnitButton = Button("Buttons/add-button.png", Vector2(this.position.x + 157f, 111f)) { unitCounterPanel.addUnit(UnitType.WATER)}
    val waterUnitButton = Button("Cards/"+getUnitName(UnitType.WATER, waterUnitData.level)+".png", Vector2(this.position.x , 83f))

    init {
//        startGameButton.onClick = { onStartGame.invoke() }
        readyButton.onClick = { onStartButtonClicked() }
        fireUnitButton.position.x += ActionPanel.WIDTH/2 - fireUnitButton.texture.width/2
        waterUnitButton.position.x += ActionPanel.WIDTH/2 - waterUnitButton.texture.width/2
        grassUnitButton.position.x += ActionPanel.WIDTH/2 - grassUnitButton.texture.width/2

        fireUnitButton.zIndex = 3
        fireUnitBackground.zIndex = 3
        fireHealthNumber.zIndex = 3
        fireMovementSpeedNumber.zIndex = 3
        addFireUnitButton.zIndex = 3
        removeFireUnitButton.zIndex = 3

        grassUnitButton.zIndex = 3
        grassUnitBackground.zIndex = 3
        grassHealthNumber.zIndex = 3
        grassMovementSpeedNumber.zIndex = 3
        addGrassUnitButton.zIndex = 3
        removeGrassUnitButton.zIndex = 3

        waterUnitButton.zIndex = 3
        waterUnitBackground.zIndex = 3
        waterHealthNumber.zIndex = 3
        waterMovementSpeedNumber.zIndex = 3
        addWaterUnitButton.zIndex = 3
        removeWaterUnitButton.zIndex = 3

    }

    private fun onStartButtonClicked() {
        println("PlanningAttackActionPanel::onStartButtonClicked")
        readyButton.hide = true
        waitingButton.hide = false
        val units = unitCounterPanel.getJsonUnitList()
        GlobalScope.launch(Dispatchers.IO) {
            val roundSimulation = ServerConnection.sendAttackInstructions(units)
            println("Received sound simulation $roundSimulation")
            Redux.jsonRoundSimulation = roundSimulation
        }
    }


    override fun die() {
        super.die()

        peekPanel.die()

        unitCounterPanel.die()

        removeFireUnitButton.die()
        addFireUnitButton.die()

        removeWaterUnitButton.die()
        addWaterUnitButton.die()

        removeGrassUnitButton.die()
        addGrassUnitButton.die()

        fireHealthNumber.die()
        fireMovementSpeedNumber.die()
        fireUnitBackground.die()
        fireUnitButton.die()
        grassHealthNumber.die()
        grassMovementSpeedNumber.die()
        grassUnitBackground.die()
        grassUnitButton.die()
        waterHealthNumber.die()
        waterMovementSpeedNumber.die()
        waterUnitBackground.die()
        waterUnitButton.die()

    }



    override fun hideUI(hide: Boolean) {
        fireHealthNumber.hide = hide
        fireMovementSpeedNumber.hide = hide
        grassHealthNumber.hide = hide
        grassMovementSpeedNumber.hide = hide
        waterHealthNumber.hide = hide
        waterMovementSpeedNumber.hide = hide
    }
}

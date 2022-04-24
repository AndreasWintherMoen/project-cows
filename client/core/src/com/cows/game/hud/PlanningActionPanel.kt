package com.cows.game.hud

import com.badlogic.gdx.math.Vector2
import com.cows.game.Application
import com.cows.game.Redux
import com.cows.game.enums.UnitType

abstract class PlanningActionPanel(): ActionPanel() {
    companion object {
        val counterPanelHeight = getPercentOfScreen(15f, Application.HEIGHT)
        val buttonPanelHeight = getPercentOfScreen(15f, Application.HEIGHT)

        private fun getPercentOfScreen(percentage: Float, screenLength: Float): Float {
            return screenLength/(percentage/100)
        }
    }
    val readyButton = Button("Buttons/ready-btn.png", Vector2(this.position.x, 0f))

    // FIRE TOWER🔥
    private val fireTowerData = Redux.jsonAvailableTowers!!.getTower(UnitType.FIRE)
    val fireTowerBackground = SmartObject("HUD/banner-fire.png", Vector2(this.position.x+32f, 323f), 1f)
    val fireTowerButton = Button("Cards/"+getUnitName(UnitType.FIRE, fireTowerData.level)+".png", Vector2(this.position.x, 330f))
    val fireDamageNumber = FontObject(fireTowerData.damage.toString(), 25, Vector2(this.position.x + 70f, 439f))
    val fireRangeNumber = FontObject(fireTowerData.damage.toString(), 25, Vector2(this.position.x + 135f, 439f))

    // GRASS TOWER🌿
    private val grassTowerData = Redux.jsonAvailableTowers!!.getTower(UnitType.GRASS)
    val grassTowerBackground = SmartObject("HUD/banner-grass.png", Vector2(this.position.x+32f , 199f), 1f)
    val waterTowerButton = Button("Cards/"+getUnitName(UnitType.GRASS, grassTowerData.level)+".png", Vector2(this.position.x , 206f))
    val grassDamageNumber = FontObject(fireTowerData.damage.toString(), 25, Vector2(this.position.x + 70f, 315f))
    val grassRangeNumber = FontObject(fireTowerData.damage.toString(), 25, Vector2(this.position.x + 135f, 315f))

    // WATER TOWER💧
    private val waterTowerData = Redux.jsonAvailableTowers!!.getTower(UnitType.WATER)
    val waterTowerBackground = SmartObject("HUD/banner-water.png", Vector2(this.position.x+32f, 75f), 1f)
    val grassTowerButton = Button("Cards/"+getUnitName(UnitType.WATER, waterTowerData.level)+".png", Vector2(this.position.x, 83f))
    val waterDamageNumber = FontObject(fireTowerData.damage.toString(), 25, Vector2(this.position.x + 70f, 192f))
    val waterRangeNumber = FontObject(fireTowerData.damage.toString(), 25, Vector2(this.position.x + 135f, 192f))

    init {
        fireTowerButton.position.x += ActionPanel.WIDTH/2 - fireTowerButton.texture.width/2
        waterTowerButton.position.x += ActionPanel.WIDTH/2 - waterTowerButton.texture.width/2
        grassTowerButton.position.x += ActionPanel.WIDTH/2 - grassTowerButton.texture.width/2
    }


    override fun die() {
        super.die()
        readyButton.die()
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

    fun getUnitName(type: UnitType, level: Int): String {
        when (type) {
            UnitType.NONE -> return ""
            UnitType.FIRE -> return when (level) {
                0 -> "charmander"
                1 -> "charmeleon"
                2 -> "charizard"
                else -> ""
            }
            UnitType.WATER -> return when (level) {
                0 -> "squirtle"
                1 -> "wartortle"
                2 -> "blastoise"
                else -> ""
            }
            UnitType.GRASS -> return when (level) {
                0 -> "bulbasaur"
                1 -> "ivysaur"
                2 -> "venosaur"
                else -> ""
            }
        }
    }

    abstract protected fun hideUI(hide: Boolean)

}
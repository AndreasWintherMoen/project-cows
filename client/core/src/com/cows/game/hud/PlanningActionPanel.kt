package com.cows.game.hud

import com.badlogic.gdx.math.Vector2
import com.cows.game.Application
import com.cows.game.enums.UnitType
import com.cows.game.map.Map
import com.cows.game.roundSimulation.rawJsonData.JsonUnit

abstract class PlanningActionPanel(): ActionPanel() {
    companion object {
        val counterPanelHeight = getPercentOfScreen(15f, Application.HEIGHT)
        val buttonPanelHeight = getPercentOfScreen(15f, Application.HEIGHT)

        private fun getPercentOfScreen(percentage: Float, screenLength: Float): Float {
            return screenLength/(percentage/100)
        }
    }
    val readyButton = Button("Buttons/ready-btn.png", Vector2(this.position.x, 0f))
    val waitingButton = Button("Buttons/waiting-button.png", Vector2(this.position.x, 0f))

    init {
        Map.init()
        readyButton.zIndex = 3
        waitingButton.zIndex = 3
        waitingButton.hide = true
    }

    override fun die() {
        super.die()
        readyButton.die()
        waitingButton.die()
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
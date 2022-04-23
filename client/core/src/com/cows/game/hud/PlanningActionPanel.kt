package com.cows.game.hud

import com.badlogic.gdx.math.Vector2
import com.cows.game.Application
import com.cows.game.enums.UnitType


abstract class PlanningActionPanel(): ActionPanel() {
    companion object {
        val counterPanelHeight = getPercentOfScreen(15f, Application.HEIGHT)
        val buttonPanelHeight = getPercentOfScreen(15f, Application.HEIGHT)

        private fun getPercentOfScreen(percentage: Float, screenLength: Float): Float {
            return screenLength/(percentage/100)
        }
    }
    val startGameButton = Button("Buttons/start-button.png", Vector2(this.position.x + 50f, 0f))

    // FIRE TOWER🔥
    val fireTowerButton = Button("Towers/charizard-panel.png", Vector2(this.position.x, ACTION_HEIGHT - 109f- UNIT_MARGIN*0))

    // GRASS TOWER🌿
    val waterTowerButton = Button("Towers/blastoise-panel.png", Vector2(this.position.x , ACTION_HEIGHT - 109f*2f - UNIT_MARGIN*1))

    // WATER TOWER💧
    val grassTowerButton = Button("Towers/venosaur-panel.png", Vector2(this.position.x, ACTION_HEIGHT - 109f*3f- UNIT_MARGIN*2))

    val unitCounterPanel = UnitCounterPanel(10)

    init {
        fireTowerButton.position.x += ActionPanel.WIDTH/2 - fireTowerButton.texture.width/2
        waterTowerButton.position.x += ActionPanel.WIDTH/2 - waterTowerButton.texture.width/2
        grassTowerButton.position.x += ActionPanel.WIDTH/2 - grassTowerButton.texture.width/2
    }

    override fun die() {
        super.die()
        unitCounterPanel.die()
        startGameButton.die()
        fireTowerButton.die()
        grassTowerButton.die()
        waterTowerButton.die()
    }

}
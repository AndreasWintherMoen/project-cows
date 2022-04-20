package com.cows.game.hud

import com.cows.game.Application


abstract class PlanningActionPanel(): ActionPanel() {
    companion object {
        val counterPanelHeight = getPercentOfScreen(15f, Application.HEIGHT)
        val buttonPanelHeight = getPercentOfScreen(15f, Application.HEIGHT)

        private fun getPercentOfScreen(percentage: Float, screenLength: Float): Float {
            return screenLength/(percentage/100)
        }
    }

}
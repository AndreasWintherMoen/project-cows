package com.cows.game.hud

import com.badlogic.gdx.math.Vector2

class ActiveGameActionPanel:ActionPanel() {
    private var coins = UnitCounterPanel.calculateAvailableUnits()
    private var coinsText = FontObject(coins.toString(), 60, Vector2(this.position.x+68f, 500f))
}
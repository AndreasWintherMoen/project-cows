package com.cows.game.hud

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.cows.game.managers.RoundManager

class ActiveGameActionPanel:ActionPanel() {
    private var coins = UnitCounterPanel.calculateAvailableUnits()
    private var coinsText = FontObject(coins.toString(), 60, Vector2(this.position.x+68f, 500f))
    private val speedupButton = Button("Buttons/speedup-button.png", Vector2(this.position.x, 0f)) { toggleSpeed() }

    init {
        coinsText.zIndex = 3
        speedupButton.zIndex = 3
    }

    private fun toggleSpeed() {
        if (RoundManager.useFastForward) normalspeed()
        else speedup()
    }

    private fun speedup() {
        RoundManager.useFastForward = true
        speedupButton.texture = Texture("Buttons/speedup-button-clicked.png")
    }

    private fun normalspeed() {
        RoundManager.useFastForward = false
        speedupButton.texture = Texture("Buttons/speedup-button.png")
    }

    override fun die() {
        super.die()
        speedupButton.die()
        coinsText.die()
    }

    override fun dispose() {
        super.dispose()
        speedupButton.dispose()
        coinsText.dispose()
    }
}
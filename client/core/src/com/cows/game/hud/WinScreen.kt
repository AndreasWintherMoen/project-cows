package com.cows.game.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2

class WinScreen {
    private val background = SmartObject("HUD/winscreen.png", Vector2(0f, 0f), 1f)
    private val quitButton = Button("HUD/quit-button.png", Vector2(403f, 100f)) { Gdx.app.exit() }

    init {
        background.zIndex = 100
        quitButton.zIndex = 100
    }
}
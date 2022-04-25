package com.cows.game.hud

import com.badlogic.gdx.math.Vector2
import com.cows.game.Application

class HealthIndicator() {
    private var firstHeart = SmartObject("HUD/heart-full.png", Vector2(10f, Application.HEIGHT - 50f), 1f)
    private var secondHeart = SmartObject("HUD/heart-full.png", Vector2(60f, Application.HEIGHT - 50f), 1f)
    private var thirdHeart = SmartObject("HUD/heart-full.png", Vector2(110f, Application.HEIGHT - 50f), 1f)
    var health = 3
        set(value) {
            field = value
            updateUI()
        }

    init {
        firstHeart.zIndex = 10
        secondHeart.zIndex = 10
        thirdHeart.zIndex = 10
    }

    private fun updateUI() {
        firstHeart.setTextureFilePath (if (health > 0) "HUD/heart-full.png" else "HUD/heart-empty.png")
        secondHeart.setTextureFilePath(if (health > 1) "HUD/heart-full.png" else "HUD/heart-empty.png")
        thirdHeart.setTextureFilePath (if (health > 2) "HUD/heart-full.png" else "HUD/heart-empty.png")
    }
}
package com.cows.game.hud

import com.badlogic.gdx.graphics.Texture
import com.cows.game.enums.UnitType
import com.cows.game.managers.RoundManager

data class UnitCounter(val iconType: UnitType, val initialCount: Int) {
    var count = initialCount
        set(value) {
            if (RoundManager.playerIsAttacker) RoundManager.gameStatus?.playerStates?.first?.coins = value
            else RoundManager.gameStatus?.playerStates?.second?.coins = value
            field = value
        }

    val iconTexture: Texture =
        when (iconType) {
            UnitType.FIRE -> Texture("Types/fire-32.png")
            UnitType.WATER -> Texture("Types/water-32.png")
            UnitType.GRASS -> Texture("Types/grass-32.png")
            UnitType.NONE -> Texture("Types/all-32.png")
        }
}
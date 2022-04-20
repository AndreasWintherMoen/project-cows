package com.cows.game.hud

import com.badlogic.gdx.graphics.Texture
import com.cows.game.enums.UnitType

data class UnitCounter(val iconType: UnitType, var count: Int) {
    val iconTexture: Texture =
        when (iconType) {
            UnitType.FIRE -> Texture("Types/fire-32.png")
            UnitType.WATER -> Texture("Types/water-32.png")
            UnitType.GRASS -> Texture("Types/grass-32.png")
            UnitType.NONE -> Texture("Types/all-32.png")
        }
}
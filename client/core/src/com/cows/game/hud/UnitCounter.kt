package com.cows.game.hud

import com.badlogic.gdx.graphics.Texture
import com.cows.game.enums.UnitType

data class UnitCounter(val iconType: UnitType, var count: Int) {
    val iconTexture: Texture =
        when (iconType) {
            UnitType.FIRE -> Texture("IndianUnit")
            UnitType.WATER -> Texture("IndianUnit")
            UnitType.GRASS -> Texture("IndianUnit")
            UnitType.NONE -> Texture("IndianUnit")
        }
}
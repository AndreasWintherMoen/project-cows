package com.cows.game.roundSimulation.rawJsonData

import com.cows.game.enums.UnitType
import com.cows.game.models.UnitModel

data class JsonUnit(
    val id: Int,
    val type: UnitType,
    val movementSpeed: Float
) {
    fun toUnitModel() = UnitModel(type, movementSpeed)
}

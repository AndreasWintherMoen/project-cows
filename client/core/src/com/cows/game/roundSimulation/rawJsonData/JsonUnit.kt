package com.cows.game.roundSimulation.rawJsonData

import com.cows.game.enums.UnitType
import com.cows.game.models.UnitModel

data class JsonUnit(
    val id: Int?,
    val type: UnitType,
    val level: Int,
    val movementSpeed: Int?,
    val health: Int?
) {
    fun toUnitModel() = UnitModel(type, level, movementSpeed!!.toFloat(), health!!)
}

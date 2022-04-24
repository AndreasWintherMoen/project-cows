package com.cows.game.roundSimulation.rawJsonData

import com.cows.game.enums.UnitType

data class JsonAvailableUnits (
    val fireUnit: JsonUnit,
    val waterUnit: JsonUnit,
    val grassUnit: JsonUnit,
){fun getUnit(type: UnitType): JsonUnit {
    return when (type) {
        UnitType.NONE -> TODO()
        UnitType.FIRE -> fireUnit
        UnitType.WATER -> waterUnit
        UnitType.GRASS -> grassUnit
    }
}}

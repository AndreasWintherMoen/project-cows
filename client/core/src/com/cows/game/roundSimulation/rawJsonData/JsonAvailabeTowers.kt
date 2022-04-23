package com.cows.game.roundSimulation.rawJsonData

import com.cows.game.enums.UnitType

data class JsonAvailableTowers (
    val fireTower: JsonTower,
    val waterTower: JsonTower,
    val grassTower: JsonTower,
) {
    fun getTower(type: UnitType): JsonTower {
        return when (type) {
            UnitType.NONE -> TODO()
            UnitType.FIRE -> fireTower
            UnitType.WATER -> waterTower
            UnitType.GRASS -> grassTower
        }
    }
}

package com.cows.game.roundSimulation.rawJsonData

import com.cows.game.map.Coordinate
import com.cows.game.enums.UnitType
import com.cows.game.models.TowerModel

data class JsonTower (
    val id: Int?,
    val type: UnitType,
    val level: Int,
    val position: Coordinate,
    val range: Int?,
    val timeBetweenAttacks: Int?,
    val damage: Int?
 ) {
    fun toTowerModel() = TowerModel(type, level, position, range!!, damage!!)
}
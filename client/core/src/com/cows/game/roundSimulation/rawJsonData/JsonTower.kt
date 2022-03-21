package com.cows.game.roundSimulation.rawJsonData

import com.cows.game.map.Coordinate
import com.cows.game.enums.TowerType
import com.cows.game.models.TowerModel

data class JsonTower (
    val id: Int,
    val type: TowerType,
    val position: Coordinate,
    val range: Float
 ) {
    fun toTowerModel() = TowerModel(type, position, range, false)
}
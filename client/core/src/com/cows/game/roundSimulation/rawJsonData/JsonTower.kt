package com.cows.game.roundSimulation.rawJsonData

import com.cows.game.Coordinate
import com.cows.game.enums.TowerType
import com.cows.game.models.TowerModel

data class JsonTower (
    val id: Int,
    val type: TowerType,
    val position: Coordinate
 ) {
    fun toTowerModel() = TowerModel(type, position)
}
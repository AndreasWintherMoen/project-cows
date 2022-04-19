package projectcows.rawJsonData

import projectcows.enums.TowerType
import projectcows.models.Coordinate
import projectcows.models.TowerModel

data class JsonTower (
    val id: Int,
    val type: TowerType,
    val position: Coordinate,
    val range: Float
 ) {
    fun toTowerModel() = TowerModel(type, position, range, false)
}
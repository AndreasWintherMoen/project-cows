package projectcows.rawJsonData

import com.cows.services.simulation.enums.UnitType
import projectcows.models.Coordinate
import projectcows.models.TowerModel

data class JsonTower (
    val id: Int,
    val type: UnitType,
    val position: Coordinate,
    val range: Float
 ) {
    fun toTowerModel() = TowerModel(type, position, range, false)
}
package projectcows.rawJsonData

import com.cows.services.simulation.enums.UnitType
import projectcows.models.Coordinate
import projectcows.models.TowerModel

data class JsonTower (
    val id: Int?,
    val type: UnitType,
    val level: Int,
    val position: Coordinate,
    val range: Int?,
    val timeBetweenAttacks: Int?,
    val damage: Int?
 )
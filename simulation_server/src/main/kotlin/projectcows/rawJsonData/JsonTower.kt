package projectcows.rawJsonData

import projectcows.enums.UnitType
import projectcows.models.Coordinate
import projectcows.simulationModels.TowerSimulationModel

data class JsonTower (
    val id: Int?,
    val type: UnitType,
    val level: Int,
    val position: Coordinate,
    val range: Int?,
    val timeBetweenAttacks: Int?,
    val damage: Int?
 )
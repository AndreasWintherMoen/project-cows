package models.simulation.json

import models.generation.Coordinate
import models.enums.UnitType

data class JsonTower (
    val id: Int?,
    val type: UnitType,
    val level: Int,
    val position: Coordinate,
    val range: Int?,
    val timeBetweenAttacks: Int?,
    val damage: Int?
 )
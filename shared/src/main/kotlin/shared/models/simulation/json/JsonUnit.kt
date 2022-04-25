package models.simulation.json

import models.enums.UnitType


data class JsonUnit(
    val id: Int?,
    val type: UnitType,
    val level: Int,
    val movementSpeed: Int?,
    val health: Int?
)

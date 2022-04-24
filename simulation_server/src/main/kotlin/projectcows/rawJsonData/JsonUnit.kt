package projectcows.rawJsonData

import projectcows.enums.UnitType

data class JsonUnit(
    val id: Int?,
    val type: UnitType,
    val level: Int,
    val movementSpeed: Int?,
    val health: Int?
)

package projectcows.rawJsonData

import projectcows.enums.UnitType
import projectcows.models.UnitModel

data class JsonUnit(
    val id: Int,
    val type: UnitType,
    val movementSpeed: Float
) {
    fun toUnitModel() = UnitModel(type, movementSpeed)
}

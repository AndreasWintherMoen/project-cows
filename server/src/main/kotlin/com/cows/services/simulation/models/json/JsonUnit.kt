package projectcows.rawJsonData

import com.cows.services.simulation.enums.UnitType
import projectcows.models.UnitModel

data class JsonUnit(
    val id: Int,
    val type: UnitType,
    val movementSpeed: Float
) {
    fun toUnitModel() = UnitModel(type, movementSpeed)
}

package projectcows.models

import com.cows.services.simulation.enums.UnitType

data class TowerModel (
    val type: UnitType,
    val tileCoordinate: Coordinate,
    var rotation: Float,
    var hasTarget: Boolean

)

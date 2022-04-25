package models.simulation

import com.cows.services.simulation.enums.UnitType
import models.generation.Coordinate

data class TowerModel (
    val type: UnitType,
    val tileCoordinate: Coordinate,
    var rotation: Float,
    var hasTarget: Boolean

)

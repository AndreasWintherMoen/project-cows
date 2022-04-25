package connection

import models.simulation.json.JsonTower
import models.simulation.json.JsonUnit
import java.util.*

data class GameCreateResponse(
    val userId: UUID,
    val gameJoinCode: String,
    val gameCodeUUID: UUID,
)

data class GameJoinResponse(
    val userId: UUID,
    val gameCodeUUID: UUID,
)

data class SimulationBody (
    val defendInstructions: List<JsonTower>,
    val attackInstructions: List<JsonUnit>,
    val path: List<IntArray>
)
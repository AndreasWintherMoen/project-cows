package projectcows.plugins

import com.google.gson.GsonBuilder
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import projectcows.enums.UnitType
import projectcows.rawJsonData.JsonAvailableUnits
import projectcows.rawJsonData.JsonTower
import projectcows.rawJsonData.JsonUnit
import projectcows.simulationModels.RoundSimulator
import projectcows.simulationModels.UnitStatsMapper

data class SimulationBody (
    val defendInstructions: List<JsonTower>,
    val attackInstructions: List<JsonUnit>,
    val path: List<IntArray>
)

fun Application.configureRouting() {
    var roundSimulator = RoundSimulator();
    val gson = GsonBuilder().setPrettyPrinting().create()

    routing {
        post("ss-cows/round/simulate"){
            val body = call.receive<SimulationBody>()
            val roundSimulation = roundSimulator.simulate(body.defendInstructions, body.attackInstructions, body.path)
            call.respondText(gson.toJson(roundSimulation), ContentType.Application.Json, HttpStatusCode.OK)
        }
    }

    routing {
        get("ss-cows/stats/units/{fireLevel}/{waterLevel}/{grassLevel}") {
            val fireLevel = call.parameters["fireLevel"]
            val waterLevel = call.parameters["waterLevel"]
            val grassLevel = call.parameters["grassLevel"]
            fireLevel ?: call.respondText( "fireLevel URL parameter not set", status = HttpStatusCode.BadRequest) {}
            waterLevel ?: call.respondText( "waterLevel URL parameter not set", status = HttpStatusCode.BadRequest) {}
            grassLevel ?: call.respondText( "grassLevel URL parameter not set", status = HttpStatusCode.BadRequest) {}
            val intFireLevel = fireLevel!!.toIntOrNull()
            val intWaterLevel = waterLevel!!.toIntOrNull()
            val intGrassLevel = grassLevel!!.toIntOrNull()
            intFireLevel ?: call.respondText( "fireLevel URL parameter not set", status = HttpStatusCode.BadRequest) {}
            intWaterLevel ?: call.respondText( "waterLevel URL parameter not set", status = HttpStatusCode.BadRequest) {}
            intGrassLevel ?: call.respondText( "grassLevel URL parameter not set", status = HttpStatusCode.BadRequest) {}
            val fireUnit = UnitStatsMapper.createJsonUnitWithStats(UnitType.FIRE, intFireLevel!!)
            val waterUnit = UnitStatsMapper.createJsonUnitWithStats(UnitType.FIRE, intWaterLevel!!)
            val grassUnit = UnitStatsMapper.createJsonUnitWithStats(UnitType.FIRE, intGrassLevel!!)
            call.respondText(gson.toJson(JsonAvailableUnits(fireUnit, waterUnit, grassUnit)))
        }
    }
}

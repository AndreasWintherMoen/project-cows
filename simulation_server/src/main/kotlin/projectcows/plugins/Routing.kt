package projectcows.plugins

import com.google.gson.GsonBuilder
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import projectcows.rawJsonData.JsonTower
import projectcows.rawJsonData.JsonUnit
import projectcows.simulationModels.RoundSimulator

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
}

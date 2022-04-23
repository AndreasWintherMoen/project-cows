package com.cows.services.simulation

import com.cows.services.simulation.models.json.JsonAvailableTowers
import com.cows.services.simulation.models.json.JsonAvailableUnits
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import com.cows.services.simulation.models.json.JsonRoundSimulation
import projectcows.rawJsonData.JsonTower
import projectcows.rawJsonData.JsonUnit
import java.text.DateFormat

data class SimulationBody (
    val defendInstructions: List<JsonTower>,
    val attackInstructions: List<JsonUnit>,
    val path: List<IntArray>
)

// Responsible for sending web requests to SS
object API {
    private val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
            }
        }
    }

    suspend fun simulate(defendInstructions: List<JsonTower>, attackInstructions: List<JsonUnit>, path: List<IntArray>): JsonRoundSimulation {
        val body = SimulationBody(defendInstructions, attackInstructions, path)
        return client.post("http://127.0.0.1:8069/ss-cows/round/simulate") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    suspend fun getUnitStats(fireLevel: Int, waterLevel: Int, grassLevel: Int): JsonAvailableUnits {
        val url = "http://127.0.0.1:8069/ss-cows/stats/units/"
//        val url = "https://api.winthermoen.no/ss-cows/stats/units/"
        return client.get("$url$fireLevel/$waterLevel/$grassLevel") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getTowerStats(fireLevel: Int, waterLevel: Int, grassLevel: Int): JsonAvailableTowers {
        val url = "http://127.0.0.1:8069/ss-cows/stats/towers/"
//        val url = "https://api.winthermoen.no/ss-cows/stats/towers/"
        return client.get("$url$fireLevel/$waterLevel/$grassLevel"){
            contentType(ContentType.Application.Json)
        }.body()
    }
}
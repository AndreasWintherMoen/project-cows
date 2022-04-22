package com.cows.services.simulation

import com.google.gson.GsonBuilder
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.ContentNegotiation
import io.ktor.client.plugins.json.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import kotlinx.coroutines.Deferred
import projectcows.rawJsonData.JsonRoundSimulation
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

    suspend fun simulate(defendInstructions: List<JsonTower>, attackInstructions: List<JsonUnit>, path: List<IntArray>): Deferred<JsonRoundSimulation> {
        val body = SimulationBody(defendInstructions, attackInstructions, path)
        return client.post("http://127.0.0.1:8069/ss-cows/round/simulate") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }
}
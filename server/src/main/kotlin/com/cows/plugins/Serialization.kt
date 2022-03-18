package com.cows.plugins

import io.ktor.serialization.gson.*
import io.ktor.server.plugins.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import java.io.File

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }

    routing {

        webSocket("/echo") {
            call.respond("Echo")
        }
        webSocket("/eventlog") {  }
        get("/json/gson") {

            val responseString = File("RoundSimulation.json").bufferedReader().readLines()
            call.respond(responseString)
        }
    }
}

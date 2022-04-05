package com.cows.plugins

import com.cows.services.ClientConnection
import com.cows.services.ConnectionMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.websocket.*
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

fun Application.configureRouting() {
    val gson = GsonBuilder().setPrettyPrinting().create()

    //  Find a solution to hinder a user to call this end-point lots of times
    routing {
        get("/game/create"){
            val user = ClientConnection()
            val gameSetup = ConnectionMapper.createGameCode(user)
            call.respondText(gson.toJson(GameCreateResponse(user.id, gameSetup.gameJoinCode, gameSetup.gameCodeUUID)), ContentType.Application.Json, HttpStatusCode.OK)
        }

        get("/game/join/{gameJoinCode}"){
            if(call.parameters["gameJoinCode"].isNullOrEmpty()) call.respond(HttpStatusCode.NotFound, "gameJoinCode not given")
            val user = ClientConnection()
            val gameCodeUUID = ConnectionMapper.createGame(user, call.parameters["gameJoinCode"]!!)
            if(gameCodeUUID == null) call.respond(HttpStatusCode.NotFound, "Game code not valid")
            call.respondText(gson.toJson(GameJoinResponse(user.id, gameCodeUUID!!)), ContentType.Application.Json, HttpStatusCode.OK);
        }
    }
}

package com.cows.plugins

import com.cows.services.ClientConnection
import com.cows.services.ConnectionMapper
import com.cows.services.shared.GameCreateResponse
import com.cows.services.shared.GameJoinResponse
import com.google.gson.GsonBuilder
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*

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

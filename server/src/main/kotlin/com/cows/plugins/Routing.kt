package com.cows.plugins

import com.cows.services.ClientConnection
import com.cows.services.ConnectionMapper
import com.cows.services.shared.GameCreateResponse
import com.cows.services.shared.GameJoinResponse
import com.cows.services.simulation.API
import com.google.gson.GsonBuilder
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import projectcows.enums.TowerType
import projectcows.enums.UnitType
import projectcows.models.Coordinate
import projectcows.rawJsonData.JsonTower
import projectcows.rawJsonData.JsonUnit

fun Application.configureRouting() {
    val gson = GsonBuilder().setPrettyPrinting().create()

    //  Find a solution to hinder a user to call this end-point lots of times
    routing {
        get("/cows/game/create"){
            val user = ClientConnection()
            val gameSetup = ConnectionMapper.createGameCode(user)
            call.respondText(gson.toJson(GameCreateResponse(user.id, gameSetup.gameJoinCode, gameSetup.gameCodeUUID)), ContentType.Application.Json, HttpStatusCode.OK)
        }

        get("/cows/game/join/{gameJoinCode}"){
            if(call.parameters["gameJoinCode"].isNullOrEmpty()) call.respond(HttpStatusCode.NotFound, "gameJoinCode not given")
            val user = ClientConnection()
            val gameCodeUUID = ConnectionMapper.createGame(user, call.parameters["gameJoinCode"]!!)
            if(gameCodeUUID == null) call.respond(HttpStatusCode.NotFound, "Game code not valid")
            call.respondText(gson.toJson(GameJoinResponse(user.id, gameCodeUUID!!)), ContentType.Application.Json, HttpStatusCode.OK);
        }
    }
}

package com.cows.game.serverConnection

import com.cows.game.roundSimulation.rawJsonData.JsonRoundSimulation
import com.cows.game.roundSimulation.rawJsonData.JsonTower
import com.cows.game.roundSimulation.rawJsonData.JsonUnit
import com.cows.game.serverConnection.shared.GameCreateResponse
import com.cows.game.serverConnection.shared.GameJoinResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import java.util.*
import io.ktor.client.features.logging.Logger
import java.time.Duration
import com.cows.game.serverConnection.shared.Message
import com.cows.game.serverConnection.shared.OpCode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

data class GameSession (
    val userUUID: UUID,
    val gameUUID: UUID)

val dotenv:Dotenv = dotenv {
    filename = "./assets/env"
}

val httpApiBase: String = dotenv["HTTP_API_BASE"] ?: "http://127.0.0.1:8080/cows"
val wsApiBase: String = dotenv["WS_API_BASE"] ?: "ws://127.0.0.1:8080/cows/ws"

object ServerConnection {


    val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(WebSockets){
            pingInterval = Duration.ofSeconds(15).seconds
            maxFrameSize = Long.MAX_VALUE
        }
        install(JsonFeature){
            serializer = GsonSerializer()
        }
    }
    var websocketSession:DefaultWebSocketSession? = null
    val gson = Gson()
    var gameSession: GameSession? = null

    private suspend fun sendGameCreateRequest(client:HttpClient): GameCreateResponse {
        return client.request<GameCreateResponse>("$httpApiBase/game/create")
    }

    suspend fun generateWebsocketClient(client: HttpClient): DefaultWebSocketSession{
        return client.webSocketSession { url("$wsApiBase") }
    }

    suspend fun createGame(){
        val createGameResponse = sendGameCreateRequest(client)
        gameSession = GameSession(createGameResponse.userId, createGameResponse.gameCodeUUID)
        val websocketClient = generateWebsocketClient(client)
        println("created game with game code ${createGameResponse.gameJoinCode}")
        websocketSession = establishGameConnection(websocketClient)
    }

    suspend fun joinGame(gameJoinCode: String){
        val joinGameResponse = sendGameJoinRequest(client,gameJoinCode)
        gameSession = GameSession(joinGameResponse.userId, joinGameResponse.gameCodeUUID)
        val websocketClient = generateWebsocketClient(client)
        websocketSession = establishGameConnection(websocketClient)
    }

    suspend fun sendAttackInstructions(unitList: List<JsonUnit>): JsonRoundSimulation {
        val data = gson.toJson(unitList)
        val message = createMessage(OpCode.INSTRUCTIONLOG, data)
        websocketSession!!.send(Message.generateWSFrame(message))
        while (true) {
            val nullableIncoming = websocketSession!!.incoming.tryReceive()
            if (nullableIncoming.isFailure || nullableIncoming.isClosed) continue
            when (val incoming = nullableIncoming.getOrThrow()) {
                is Frame.Text -> {
                    val message = Message.retrieveWSMessage(incoming)
                    println(message)
                    if (message!!.opCode == OpCode.AWAIT){
                        println("Received await message...")
                        continue
                    } else if (message!!.opCode == OpCode.EVENTLOG) {
                        val defendInstructionsType = object : TypeToken<JsonRoundSimulation>() {}.type
                        val roundSimulation: JsonRoundSimulation = gson.fromJson(message.data!!, defendInstructionsType)
                        return roundSimulation
                    }
                }
                else -> {
                    println("Not text frame")
                    println(incoming)
                }
            }
        }
    }

    suspend fun sendDefendInstructions(towerList: List<JsonTower>): JsonRoundSimulation {
        val data = gson.toJson(towerList)
        val message = createMessage(OpCode.INSTRUCTIONLOG, data)
        websocketSession!!.send(Message.generateWSFrame(message))
        while (true) {
            val nullableIncoming = websocketSession!!.incoming.tryReceive()
            if (nullableIncoming.isFailure || nullableIncoming.isClosed) continue
            when (val incoming = nullableIncoming.getOrThrow()) {
                is Frame.Text -> {
                    val message = Message.retrieveWSMessage(incoming)
                    println(message)
                    if (message!!.opCode == OpCode.AWAIT){
                        println("Received await message...")
                        continue
                    } else if (message!!.opCode == OpCode.EVENTLOG) {
                        val defendInstructionsType = object : TypeToken<JsonRoundSimulation>() {}.type
                        val roundSimulation: JsonRoundSimulation = gson.fromJson(message.data!!, defendInstructionsType)
                        return roundSimulation
                    }
                }
                else -> {
                    println("Not text frame")
                    println(incoming)
                }
            }
        }
    }

    suspend fun establishGameConnection(wsSession:DefaultWebSocketSession): DefaultWebSocketSession{
        var isConnected = false
        val connectMessage = createMessage(OpCode.CONNECT, null)
        wsSession.send(
            Message.generateWSFrame(
                connectMessage
            )
        )
        while (!isConnected){
            val nullableIncoming = wsSession.incoming.tryReceive()
            if (nullableIncoming.isFailure || nullableIncoming.isClosed) continue
            when (val incoming = nullableIncoming.getOrThrow()) {
                is Frame.Text -> {
                    val message = Message.retrieveWSMessage(incoming)
                    println(message)
                    if (message!!.opCode == OpCode.CONNECTED){
                        isConnected = true
                    }
                }
                else -> {
                    println("Not text frame")
                    println(incoming)
                }
            }
        }
        return wsSession
    }

    suspend fun sendGameJoinRequest(client: HttpClient,gameJoinCode: String): GameJoinResponse {
        val response: GameJoinResponse = client.request("$httpApiBase/game/join/${gameJoinCode}")
        return response
    }

    private fun createMessage(opCode: OpCode, data: String?): Message {
        gameSession?.let {
            return Message(it.userUUID, it.gameUUID, opCode, data)
        } ?: run {
            throw Exception("No game details set (you must join a game first)")
        }
    }
}
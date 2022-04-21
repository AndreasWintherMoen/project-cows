package com.cows.game.serverConnection

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
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

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

    private suspend fun sendGameCreateRequest(client:HttpClient): GameCreateResponse {
        return client.request<GameCreateResponse>("$httpApiBase/game/create")
    }

    suspend fun generateWebsocketClient(client: HttpClient): DefaultWebSocketSession{
        return client.webSocketSession { url("$wsApiBase") }
    }

    suspend fun createGame(){
        val createGameResponse = sendGameCreateRequest(client)
        val websocketClient = generateWebsocketClient(client)
        println("created game with game code ${createGameResponse.gameJoinCode}")
        websocketSession = establishGameConnection(websocketClient,createGameResponse.userId,createGameResponse.gameCodeUUID)
    }

    suspend fun joinGame(){
        val joinGameResponse = sendGameJoinRequest(client,gameJoinCode)
        val websocketClient = generateWebsocketClient(client)
        websocketSession = establishGameConnection(websocketClient,joinGameResponse.userId,joinGameResponse.gameCodeUUID)
    }

    suspend fun establishGameConnection(wsSession:DefaultWebSocketSession, userUUID: UUID, gameUUID: UUID): DefaultWebSocketSession{
        var isConnected = false
        val connectMessage = Message(userUUID,gameUUID, OpCode.CONNECT,null)
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
}
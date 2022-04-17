package com.cows.game.serverConnection

import com.cows.game.serverConnection.shared.GameCreateResponse
import com.cows.game.serverConnection.shared.GameJoinResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import java.util.*
import io.ktor.client.features.logging.Logger
import java.time.Duration
import com.cows.game.serverConnection.shared.Message
import com.cows.game.serverConnection.shared.OpCode

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

    suspend fun sendGameCreateRequest(client:HttpClient): GameCreateResponse {
        val response: GameCreateResponse = client.request("http://0.0.0.0:8080/game/create")
        return response
    }

    suspend fun generateWebsocketClient(client: HttpClient): DefaultWebSocketSession{
        return client.webSocketSession(method = HttpMethod.Get, host = "0.0.0.0", port = 8080, path = "/ws")
    }

    suspend fun createGame(client: HttpClient){
        val createGameResponse = sendGameCreateRequest(client)
        val websocketClient = generateWebsocketClient(client)
        websocketSession = establishGameConnection(websocketClient,createGameResponse.userId,createGameResponse.gameCodeUUID)

    }

    suspend fun joinGame(client: HttpClient, gameJoinCode: String){
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
            when (val incoming = wsSession.incoming.receive()){
                is Frame.Text -> {
                    val message = Message.retrieveWSMessage(incoming)
                    if (message!!.opCode == OpCode.CONNECTED){
                        isConnected = true
                    }
                }
            }
        }
        return wsSession
    }

    suspend fun sendGameJoinRequest(client: HttpClient,gameJoinCode: String): GameJoinResponse {
        val response: GameJoinResponse = client.request("http://0.0.0.0:8080/game/join/${gameJoinCode}")
        return response
    }
}
package com.cows.game.serverConnection

import com.google.gson.GsonBuilder
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
import kotlinx.coroutines.*
import java.time.Duration

data class GameCreateResponse(
    val userId: UUID,
    val gameJoinCode: String,
    val gameCodeUUID: UUID,
)

data class GameJoinResponse(
    val userId: UUID,
    val gameCodeUUID: UUID,
)
// TODO: Implement a more intuitive separation between connect and join
enum class OpCode{
    CONNECT,
    JOIN,
    AWAIT,
    CONNECTED,
    INSTRUCTIONLOG,
    EVENTLOG,
}


// Todo: Implement data in its own class.
data class Message(val userUUID: UUID,val gameUUID:UUID, val opCode:OpCode, val data:String?)

object ServerConnection {
    val gson = GsonBuilder().setPrettyPrinting().create()
    private val client = HttpClient(CIO) {
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
    var userId: UUID? = null
    var gameJoinCode: String? = null
    var gameCodeUUID: UUID? = null

    // User will automatically join game after creating it
    // TODO: Send user to a websocket connection to check when user 2 joins the game
    suspend fun createGame(client:HttpClient): GameCreateResponse {
        val response: GameCreateResponse = client.request("http://0.0.0.0:8080/game/create")
        userId = response.userId
        gameJoinCode = response.gameJoinCode
        gameCodeUUID = response.gameCodeUUID

        return response
    }

    suspend fun generateWebsocketConnection(client: HttpClient): DefaultWebSocketSession{
        return client.webSocketSession(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/ws")
    }

    suspend fun establishWSConnection(wsSession:DefaultWebSocketSession, userUUID: UUID, gameUUID: UUID): DefaultWebSocketSession{
        var isConnected = false
        val connectMessage = Message(userUUID,gameUUID,OpCode.CONNECT,null)
        wsSession.send(
            generateWSFrame(
                connectMessage
            )
        )
        while (!isConnected){
            when (val incoming = wsSession.incoming.receive()){
                is Frame.Text -> {
                    val message = retrieveWSMessage(incoming)
                    if (message!!.opCode == OpCode.CONNECTED){
                        isConnected = true
                    }
                }
            }
        }
        return wsSession
    }



    // TODO: Send user to a websocket connection to automatically get game updates
    suspend fun joinGame(gameJoinCode: String): GameJoinResponse {
        val response: GameJoinResponse = client.request("http://0.0.0.0:8080/game/join/${gameJoinCode}")
        userId = response.userId
        gameCodeUUID = response.gameCodeUUID
        return response
    }

    fun clearGameData() {
        userId = null
        gameJoinCode = null
        gameCodeUUID = null
    }


    //TODO: Implement into message class
    suspend fun generateWSFrame(message: Message):Frame{
        return Frame.Text(
            gson.toJson(
                message
            )
        )
    }

    //TODO: Implement into message class
    suspend fun retrieveWSMessage(frame:Frame.Text):Message?{
        return gson.fromJson(frame.readText(),Message::class.java)
    }
}
package com.cows.game.serverConnection

import com.cows.game.roundSimulation.rawJsonData.*
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
import io.ktor.utils.io.*
import io.ktor.utils.io.core.internal.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

data class GameSession (
    val userUUID: UUID,
    val gameUUID: UUID)

val dotenv:Dotenv = dotenv {
    filename = "./assets/env"
}

val httpApiBase: String = dotenv["HTTP_API_BASE"] ?: "http://127.0.0.1:8080/cows"
val wsApiBase: String = dotenv["WS_API_BASE"] ?: "ws://127.0.0.1:8080/ws-cows"

object ServerConnection {


    val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(WebSockets)
        install(JsonFeature){
            serializer = GsonSerializer()
        }
    }
    var websocketSession:DefaultWebSocketSession? = null
    val gson = Gson()
    var gameSession: GameSession? = null

    private suspend fun sendGameCreateRequest(client:HttpClient): GameCreateResponse {
        println("Sending request to $httpApiBase/game/create")
        return client.request<GameCreateResponse>("$httpApiBase/game/create")
    }

    private suspend fun generateWebsocketClient(client: HttpClient): DefaultWebSocketSession{
        return client.webSocketSession { url("$wsApiBase") }
    }

    suspend fun createGame(): String {
        return withContext(Dispatchers.IO) {
            val createGameResponse = sendGameCreateRequest(client)
            gameSession = GameSession(createGameResponse.userId, createGameResponse.gameCodeUUID)
            websocketSession = generateWebsocketClient(client)
            println("created game with game code ${createGameResponse.gameJoinCode}")
            createGameResponse.gameJoinCode
        }
    }

    suspend fun connectToActiveGame() {
        println("Connect to active game")
        websocketSession?.let {
            websocketSession = establishGameConnection(it)
        } ?: run {
            println("null websocket session!")
        }
    }

    suspend fun joinGame(gameJoinCode: String) {
        val joinGameResponse = sendGameJoinRequest(client,gameJoinCode)
        gameSession = GameSession(joinGameResponse.userId, joinGameResponse.gameCodeUUID)
        websocketSession = generateWebsocketClient(client)
        connectToActiveGame()
    }

    suspend fun getAvailableUnits(): JsonAvailableUnits {
        val message = createMessage(OpCode.AVAILABLEUNITS, null)
        websocketSession!!.send(Message.generateWSFrame(message))
        while (true) {
        val nullableIncoming = websocketSession!!.incoming.tryReceive()
        if (nullableIncoming.isFailure || nullableIncoming.isClosed) continue
            when (val incoming = nullableIncoming.getOrThrow()) {
                is Frame.Text -> {
                    val message = Message.retrieveWSMessage(incoming)
                    println(message)
                    if (message!!.opCode == OpCode.AVAILABLEUNITS){
                        val unitsType = object : TypeToken<JsonAvailableUnits>() {}.type
                        val units: JsonAvailableUnits = gson.fromJson(message.data!!, unitsType)
                        return units
                    }
                }
                else -> {
                    println("Not text frame")
                    println(incoming)
                }
            }
        }
    }

    suspend fun getAvailableTowers(): JsonAvailableTowers {
        val message = createMessage(OpCode.AVAILABLETOWERS, null)
        websocketSession!!.send(Message.generateWSFrame(message))
        while (true) {
            val nullableIncoming = websocketSession!!.incoming.tryReceive()
            if (nullableIncoming.isFailure || nullableIncoming.isClosed) continue
            when (val incoming = nullableIncoming.getOrThrow()) {
                is Frame.Text -> {
                    val message = Message.retrieveWSMessage(incoming)
                    println(message)
                    if (message!!.opCode == OpCode.AVAILABLETOWERS){
                        val towersType = object : TypeToken<JsonAvailableTowers>() {}.type
                        val towers: JsonAvailableTowers = gson.fromJson(message.data!!, towersType)
                        println("parsed towers")
                        println(towers)
                        return towers
                    }
                }
                else -> {
                    println("Not text frame")
                    println(incoming)
                }
            }
        }
    }

    suspend fun sendAttackInstructions(unitList: List<JsonUnit>): JsonRoundSimulation {
        println("Send attack instructions")
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
        println("Send defend instructions")
        val data = gson.toJson(towerList)
        println(data)
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
            println("Received frame ${Frame}")
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


    val mutex = Mutex()
    val methodsToRun = LinkedList<() -> Unit>()

    fun runAsyncMethod(method: () -> Unit) {
        println("runAsyncMethod")
//        mutex.withLock { methodsToRun.add(method) }
        methodsToRun.add(method)
        println("done with runAsyncMethod")
    }

    suspend fun launchInfiniteMethodRunnerLoop() {
        println("launchInfiniteMethodRunnerLoop")
        mutex.withLock { methodsToRun.remove().invoke() }
        println("done with launchInfiniteMethodRunnerLoop")
    }



}
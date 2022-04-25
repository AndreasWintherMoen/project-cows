package com.cows.plugins

import com.cows.map.Coordinate
import com.cows.models.Message
import com.cows.models.OpCode
import com.cows.services.ClientConnection
import com.cows.services.ConnectionMapper
import com.cows.services.simulation.API
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.cows.services.simulation.models.json.JsonRoundSimulation
import projectcows.rawJsonData.JsonTower
import projectcows.rawJsonData.JsonUnit
import java.util.*

private var connectionMap: MutableMap<ClientConnection, DefaultWebSocketSession> = Collections.synchronizedMap(mutableMapOf())
private val gson = GsonBuilder().setPrettyPrinting().create()

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(200)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/ws-cows") {
            // Checks if user already has a connection
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val message:Message = gson.fromJson(frame.readText(),Message::class.java)
                        when (message.opCode) {
                            OpCode.CONNECT -> handleConnect(message,this)
                            OpCode.AWAIT -> TODO()
                            OpCode.CONNECTED -> TODO()
                            OpCode.INSTRUCTIONLOG -> handleInstructionLog(message, this)
                            OpCode.EVENTLOG -> TODO()
                            OpCode.GAMESTATE -> handleGameStateRequest(message, this)
                        }
                    }
                    else -> { println("Frame type $frame not found in when statement in /cows/ws endpoint") }
                }
            }
        }
    }
}

data class MapData(
    val path: ArrayList<Coordinate>
)

suspend fun handleGameStateRequest(message: Message, userWebSocketSession: DefaultWebSocketServerSession) {
    println("handleGameStateRequest")
    val userConnection:ClientConnection? = getClientFromConnectMessage(message)
    userConnection ?: run {
        userWebSocketSession.close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, "You have not created or joined a game yet!"))
        return
    }
    connectionMap[userConnection] = userWebSocketSession
    val game = ConnectionMapper.getGameFromUUID(message.gameUUID)
    val message = Message(
        userUUID = message.userUUID,
        gameUUID = message.gameUUID,
        OpCode.GAMESTATE,
        gson.toJson(game!!.gameState)
    )
    val messageString = gson.toJson(message)
    userWebSocketSession.outgoing.send(Frame.Text(messageString))
}

suspend fun handleConnect(message: Message, userWebSocketSession: DefaultWebSocketServerSession){
    println("Handle connect")
    val userConnection:ClientConnection? = getClientFromConnectMessage(message)
    userConnection ?: run {
        userWebSocketSession.close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, "You have not created or joined a game yet!"))
        return
    }

    connectionMap[userConnection] = userWebSocketSession
    val responseMessage: Message =
        if (ConnectionMapper.areGameSlotsFilled(gameUUID = message.gameUUID)) {
            val creatorConnection = getCreatorConnection(message.gameUUID, message.userUUID)

            connectionMap[creatorConnection]!!.outgoing.send(
                Frame.Text(
                    gson.toJson(
                        Message(
                            userUUID = creatorConnection.id,
                            gameUUID = message.gameUUID,
                            opCode = OpCode.CONNECTED,
                            null
                        )
                    )
                )
            )

            Message(
                userUUID = message.userUUID,
                gameUUID = message.gameUUID,
                OpCode.CONNECTED,
                null
            )

        } else {
            Message(
                userUUID = message.userUUID,
                gameUUID = message.gameUUID,
                OpCode.AWAIT,
                null
            )
        }
    val messageString = gson.toJson(responseMessage)
    userWebSocketSession.outgoing.send(Frame.Text(messageString))
}

suspend fun handleInstructionLog(message: Message, userWebSocketSession: DefaultWebSocketServerSession) {
    val userConnection:ClientConnection? = getClientFromConnectMessage(message)
    userConnection ?: run {
        userWebSocketSession.close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, "You have not created or joined a game yet!"))
        return
    }
    val game = ConnectionMapper.getGameFromUUID(message.gameUUID)
    game ?: run {
        userWebSocketSession.close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Could not find game session"))
        return
    }
    val roundSimulation: JsonRoundSimulation? = if (game.isPlayerAttacker(message.userUUID)) {
        val attackInstructionsType = object : TypeToken<List<JsonUnit>>() {}.type
        val data: List<JsonUnit> = gson.fromJson(message.data!!, attackInstructionsType)
        game.addAttackInstructions(data)
    } else {
        val defendInstructionsType = object : TypeToken<List<JsonTower>>() {}.type
        val data: List<JsonTower> = gson.fromJson(message.data!!, defendInstructionsType)
        game.addDefendInstructions(data)
    }
    val responseMessage: Message = if (roundSimulation == null) {
        Message(message.userUUID, message.gameUUID, OpCode.AWAIT, null)
    } else {
        val roundSimulationString = gson.toJson(roundSimulation)
        val otherUserId = game.getOtherClientConnection(message.userUUID)
        connectionMap[otherUserId]!!.outgoing.send(
            Frame.Text(
                gson.toJson(
                    Message(
                        userUUID = otherUserId.id,
                        gameUUID = message.gameUUID,
                        opCode = OpCode.EVENTLOG,
                        roundSimulationString
                    )
                )
            )
        )
        Message(message.userUUID, message.gameUUID, OpCode.EVENTLOG, roundSimulationString)
    }
    val messageString = gson.toJson(responseMessage)
    userWebSocketSession.outgoing.send(Frame.Text(messageString))
}

fun getClientFromConnectMessage(message: Message): ClientConnection? {
    return ConnectionMapper.getClientConnectionInGame(message.userUUID, message.gameUUID)
}

fun getCreatorConnection(gameUUID: UUID, joinerUUID:UUID): ClientConnection {
    val gameConnections = ConnectionMapper.getClientConnectionsFromGame(gameUUID)
    return if (gameConnections.first.id != joinerUUID) gameConnections.first else gameConnections.second
}



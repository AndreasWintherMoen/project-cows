package com.cows.plugins

import ch.qos.logback.core.net.server.Client
import com.cows.models.Message
import com.cows.models.OpCode
import com.cows.services.ClientConnection
import com.cows.services.ConnectionMapper
import com.google.gson.GsonBuilder
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import java.util.*

private var connectionMap: MutableMap<ClientConnection,DefaultWebSocketSession> = Collections.synchronizedMap(mutableMapOf())
private val gson = GsonBuilder().setPrettyPrinting().create()

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/cows/ws") {
            // Checks if user already has a connection
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val message:Message = gson.fromJson(frame.readText(),Message::class.java)
                        if (message.opCode == OpCode.CONNECT){
                            handleConnect(message,this)
                        }
                    }
                }
            }
        }
    }
}

suspend fun handleConnect(message: Message, userWebSocketSession: DefaultWebSocketServerSession){

    val userConnection:ClientConnection? = getClientFromConnectMessage(message)
    if (userConnection != null){
        connectionMap[userConnection] = userWebSocketSession
        val responseMessage:Message = if (ConnectionMapper.areGameSlotsFilled(gameUUID = message.gameUUID)){
            val creatorConnection = getCreatorConnection(message.gameUUID, message.userUUID)

            connectionMap[creatorConnection]!!.outgoing.send(Frame.Text(
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
                userUUID= message.userUUID,
                gameUUID = message.gameUUID,
                OpCode.CONNECTED,
                null)

        } else{
            Message(
                userUUID= message.userUUID,
                gameUUID = message.gameUUID,
                OpCode.AWAIT,
                null)
        }
        val messageString = gson.toJson(responseMessage)
        userWebSocketSession.outgoing.send(Frame.Text(messageString))
    } else{
        userWebSocketSession.close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, "You have not created or joined a game yet!"))
    }
}


fun getClientFromConnectMessage(message: Message): ClientConnection? {
    return ConnectionMapper.getClientConnectionInGame(message.userUUID, message.gameUUID)
}

fun getCreatorConnection(gameUUID: UUID, joinerUUID:UUID): ClientConnection {
    val gameConnections = ConnectionMapper.getClientConnectionsFromGame(gameUUID)
    return if (gameConnections.first.id != joinerUUID) gameConnections.first else gameConnections.second
}



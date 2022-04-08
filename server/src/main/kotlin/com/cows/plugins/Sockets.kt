package com.cows.plugins

import com.cows.services.ClientConnection
import com.cows.services.ConnectionMapper
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.routing.*
import java.util.*


fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        var connectionMap: MutableMap<DefaultWebSocketSession, ClientConnection> = Collections.synchronizedMap(mutableMapOf())
        webSocket("/connect") {
            // Checks if user already has a connection
            if (connectionMap.containsKey(this)){
                outgoing.send(Frame.Text("You already have a valid connection!"))
            }
            else{
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            // User input should be " <userUUIDString>:<gameUUIDString "
                            val userConnection:ClientConnection? = getClientFromFrameText(frame.readText())
                            if (userConnection != null){
                                outgoing.send(Frame.Text("You are now logged in!"))
                                connectionMap[this] = userConnection
                            } else{
                                close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, "You have not created or joined a game yet!"))
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getClientFromFrameText(frameText: String): ClientConnection? {
    val userUUIDList = getUserUUIDList(frameText)
    return ConnectionMapper.getUserInGame(userUUIDList[0], userUUIDList[1])
}


fun getUserUUIDList(frameText:String) : List<UUID>{
    return frameText.split(":").map { s: String -> UUID.fromString(s) }
}




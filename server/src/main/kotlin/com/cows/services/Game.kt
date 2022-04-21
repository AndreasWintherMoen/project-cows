package com.cows.services

import java.util.concurrent.atomic.AtomicInteger
import java.util.*


class Game(val gameConnections:Pair<ClientConnection,ClientConnection>) {
    companion object {
        // TODO: Implement a UUID-system instead of a counter
        var lastId = AtomicInteger(0)
    }


    // Helper function for checking if a clientConnection sending an instructionList is a player in the game
    fun isConnectionInGame(clientConnection: ClientConnection): Boolean{
        return (gameConnections.first == clientConnection || gameConnections.second == clientConnection)
    }

    fun isConnectionInGame(userUUID: UUID): Boolean{
        return (gameConnections.first.id == userUUID || gameConnections.second.id == userUUID)
    }

    fun getClientConnection(userUUID:UUID) : ClientConnection {
        return if (gameConnections.first.id == userUUID) gameConnections.first else gameConnections.second
    }



    private val Id:Int = lastId.getAndIncrement()





}
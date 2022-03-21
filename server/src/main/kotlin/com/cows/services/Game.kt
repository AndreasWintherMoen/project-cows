package com.cows.services

import ch.qos.logback.core.net.server.Client
import java.util.concurrent.atomic.AtomicInteger
import java.util.*


class Game(private val gameConnections:Pair<ClientConnection,ClientConnection>) {
    companion object {
        // TODO: Implement a UUID-system instead of a counter
        var lastId = AtomicInteger(0)
    }


    // Helper function for checking if a clientConnection sending an instructionList is a player in the game
    fun isConnectionInGame(clientConnection: ClientConnection): Boolean{
        return (gameConnections.first == clientConnection || gameConnections.second == clientConnection)
    }



    private val Id:Int = lastId.getAndIncrement()





}
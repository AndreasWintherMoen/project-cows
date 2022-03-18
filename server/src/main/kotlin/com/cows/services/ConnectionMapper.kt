package com.cows.services

import io.ktor.network.sockets.*
import java.util.*


// This object will map connections to the game id's
// TODO: Create serializer and verification for the recieved instructionlists to prevent a user from making moves for the other
// This object compares if the incoming connection is valid for the requested game and if so,
// maps the requested instruction list to the incoming game.
object ConnectionMapper {


    // This is on the format of <GameId> -> Pair (Connection, Connection) and is used to verify that the requested gameId
    // actually comes from one of the clients
    private var connectionGameMap:MutableMap<UUID,Pair<Connection,Connection>> = Collections.synchronizedMap(mutableMapOf())


    // This map is used to map the gameId to the actual game, should the client be able to prove that they are
    // one of the clients in the game.
    var gameMap:MutableMap<UUID,Game> = Collections.synchronizedMap(mutableMapOf())


    fun addConnection(connection1: Connection, connection2: Connection){
        val gameUUID = UUID.randomUUID()
        val newGame = Game()

        connectionGameMap[gameUUID]= Pair(connection1,connection2)
        gameMap[gameUUID] = newGame
    }

    fun removeConnection(uuid:UUID){
        connectionGameMap.remove(uuid)
        gameMap.remove(uuid)
    }






}
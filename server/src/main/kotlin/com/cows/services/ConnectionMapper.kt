package com.cows.services

import ch.qos.logback.core.net.server.Client
import io.ktor.util.date.*
import java.util.*


// This object will map connections to the game id's
// TODO: Create serializer and verification for the recieved instructionlists to prevent a user from making moves for the other
// This object compares if the incoming connection is valid for the requested game and if so,
// maps the requested instruction list to the incoming game.
object ConnectionMapper {

    // This is on the format of <GameId> -> Pair (Connection, Connection) and is used to verify that the requested gameId
    // actually comes from one of the clients
    private var clientConnectionGameMap:MutableMap<UUID,Pair<ClientConnection,ClientConnection>> = Collections.synchronizedMap(mutableMapOf())


    // This map is used to map the gameId to the actual game, should the client be able to prove that they are
    // one of the clients in the game.
    private var gameMap:MutableMap<UUID,Game> = Collections.synchronizedMap(mutableMapOf())

    // This map will list the currently active and valid gameCodes
    private var gameCodeMap:MutableMap<Int,Pair<GameCode,ClientConnection>> = Collections.synchronizedMap(mutableMapOf())


    fun addConnection(clientConnection1: ClientConnection, clientConnection2: ClientConnection){
        val gameUUID = UUID.randomUUID()
        val newGame = Game()

        clientConnectionGameMap[gameUUID]= Pair(clientConnection1,clientConnection2)
        gameMap[gameUUID] = newGame
    }

    // This function will create a game code to be used by the user and potentially used by another user
    fun createGameCode(clientConnection:ClientConnection) : GameCode{
        return GameCode(generateGameCode())
    }


    // This function generates a gameCode which can be used, and does not exist in the set of currently available game codes
    private fun generateGameCode(): Int {
        var proposedCode:Int =  (0..99999).random()

        // If the proposedCode is in the existing set of valid codes, then produce a new one.
        while(gameCodeMap.containsKey(proposedCode)){
            proposedCode =  (0..99999).random()
        }
        return proposedCode
    }


    // Checks if the inserted integer code is valid, based on the timestamp being not more than
    // 15 minutes old and if it is in the set of valid codes.
    // If so, it returns a clientConnection
    fun getGameCodeConnection(gameCodeInteger:Int) : ClientConnection?{
        // This gameCode may be null if the gameCode does not exist in the set of valid gameCodes
        val gameCodeMapEntry:Pair<GameCode,ClientConnection>? = gameCodeMap[gameCodeInteger]
        if (gameCodeMapEntry != null){
            return if (
                    // Checks if the gameCodeMapEntry is no less than 15 minutes old
                    getTimeMillis()- gameCodeMapEntry.first.timeOfCreation < 900000
                    ){
                gameCodeMapEntry.second
            }
            else{
                null
            }
        }
        else{
            // If the gameCodeMapEntry is null, it does not exist in the gameCodeMap and is therefore not valid.
            // It may have been removed
            return null
        }
    }


    fun removeConnection(uuid:UUID){
        clientConnectionGameMap.remove(uuid)
        gameMap.remove(uuid)
    }






}
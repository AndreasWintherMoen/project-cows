package com.cows.services

import ch.qos.logback.core.net.server.Client
import io.ktor.util.date.*
import java.util.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope


// This object will map connections to the game id's
// TODO: Create serializer and verification for the received instructionlists to prevent a user from making moves for the other
// TODO: Create an efficient way of checking if a user requesting a gameCode is in an active game.
// This object compares if the incoming connection is valid for the requested game and if so,
// maps the requested instruction list to the incoming game.
class ConnectionMapper {

    // This is the timeout value for the gameCodes. 900 000 is 15 minutes
    private val gameCodeTimeoutMillis = 900000


    // This map is used to map the gameId to the actual game, should the client be able to prove that they are
    // one of the clients in the game.
    var gameMap:MutableMap<UUID,Game> = Collections.synchronizedMap(mutableMapOf())


    // This is an API function which takes in a new connecting client and a gameCodeInteger to create a game
    // The function will check if the requested gameCode is valid and if so will create a UUID for
    // the newly instantiated game. It will then use this Game-UUID as a key in the gameMap,
    // where the value is a Game.
    fun createGame(clientConnection: ClientConnection, gameCodeInteger: Int): UUID?{
        val gameCode:GameCode? = getGameCode(gameCodeInteger)
        return if (gameCode != null){
            val gameCodeUUID = UUID.randomUUID()
            gameMap[gameCodeUUID] = Game(Pair(gameCode.creator,clientConnection))
            gameCodeUUID
        } else{
            // If this returns null, then the gameCodeInteger is not valid.
            // This may occur if the code was used more than 15 minutes after the creation
            // or if the code did not exist in the GameCodeMap in the first place
            null
        }
    }

    // This map will list the currently active and valid gameCodes
    private var gameCodeMap:MutableMap<Int,GameCode> = Collections.synchronizedMap(mutableMapOf())



    // This API function is used by the API endpoints to generate a valid code.
    // This function will generate a valid GameCodeInteger and a valid GameCode based on this integer
    // It will then store this GameCode in the GameCodeMap with the key being the GameCodeInteger and the value being the GameCode
    fun createGameCode(clientConnection:ClientConnection) : Int {
        // Generates the gameCodeInteger and initializes the gameCode with the creator connection
        val gameCodeInteger:Int = generateGameCodeInteger()
        gameCodeMap[gameCodeInteger] = GameCode(gameCodeInteger,clientConnection)
        return gameCodeInteger
    }


    // This is an internal function which generates a gameCode which can be used, and does not exist in the set of currently available game codes
    private fun generateGameCodeInteger(): Int {
        var proposedCode:Int =  (0..99999).random()

        // If the proposedCode is in the existing set of valid codes, then produce a new one.
        while(gameCodeMap.containsKey(proposedCode)){
            proposedCode =  (0..99999).random()
        }

        return proposedCode
    }


    // Checks if the inserted integer code is valid, based on the timestamp being not more than
    // 15 minutes old and if it is in the set of valid codes.
    // If so, it returns a valid GameCode instance.
    private fun getGameCode(gameCodeInteger:Int) : GameCode?{
        // This gameCode may be null if the gameCode does not exist in the set of valid gameCodes
        val gameCodeMapEntry:GameCode? = gameCodeMap[gameCodeInteger]
        if (gameCodeMapEntry != null){
            return if (
                // Checks if the gameCodeMapEntry is no less than 15 minutes old
                getTimeMillis()- gameCodeMapEntry.timeOfCreation < gameCodeTimeoutMillis
            ){
                gameCodeMapEntry
            } else{
                // If this returns null, then the gameCodeMapEntry has been created later than 15 minutes and therefore is not valid.
                // Thus the code must also be removed as it should not be in the map and the method returns null.
                gameCodeMap.remove(gameCodeInteger)
                null
            }
        }
        else{
            // If the gameCodeMapEntry is null, it does not exist in the gameCodeMap and is therefore not valid.
            return null
        }
    }

    private val gameCodeGarbageCollector = GameCodeGarbageCollector(gameCodeMap,gameCodeTimeoutMillis,30000L)


}
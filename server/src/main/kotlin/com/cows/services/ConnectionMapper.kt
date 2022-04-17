package com.cows.services

import io.ktor.util.date.*
import java.security.MessageDigest
import java.util.*

// This object will map connections to the game id's
// TODO: Create serializer and verification for the received instructionlists to prevent a user from making moves for the other
// TODO: Create an efficient way of checking if a user requesting a gameCode is in an active game.
// This object compares if the incoming connection is valid for the requested game and if so,
// maps the requested instruction list to the incoming game.
object ConnectionMapper {

    // This is the timeout value for the gameCodes. 900 000 is 15 minutes
    private val gameCodeTimeoutMillis = 900000

    // This map is used to map the gameId to the actual game, should the client be able to prove that they are
    // one of the clients in the game.

    private var gameMap: MutableMap<UUID, Game> = Collections.synchronizedMap(mutableMapOf())

    // This is an API function which takes in a new connecting client and a gameCodeInteger to create a game
    // The function will check if the requested gameCode is valid and if so will create a UUID for
    // the newly instantiated game. It will then use this Game-UUID as a key in the gameMap,
    // where the value is a Game.
    fun createGame(clientConnection: ClientConnection, gameJoinCode: String): UUID? {
        val gameCode: GameCode? = getGameCode(gameJoinCode)
        return if (gameCode != null) {
            gameMap[gameCode.gameCodeUUID] = Game(Pair(gameCode.creator, clientConnection))
            gameCode.gameCodeUUID
        } else {
            // If this returns null, then the gameCodeInteger is not valid.
            // This may occur if the code was used more than 15 minutes after the creation
            // or if the code did not exist in the GameCodeMap in the first place
            null
        }
    }

    // This map will list the currently active and valid gameCodes
    private var gameCodeMap: MutableMap<String, GameCode> =
        Collections.synchronizedMap(mutableMapOf())

    // This API function is used by the API endpoints to generate a valid code.
    // This function will generate a valid GameCodeInteger and a valid GameCode based on this integer
    // It will then store this GameCode in the GameCodeMap with the key being the GameCodeInteger and the value being the GameCode
    fun createGameCode(clientConnection: ClientConnection): GameCode {
        // Generates the gameCodeInteger and initializes the gameCode with the creator connection
        val gameCodeUUID = UUID.randomUUID()
        val gameJoinCode = generateGameCode(gameCodeUUID)
        val gameSetup = GameCode(gameJoinCode, gameCodeUUID, clientConnection)
        gameCodeMap[gameJoinCode] = gameSetup
        return gameSetup
    }

    fun getClientConnectionInGame(userUUID: UUID,gameUUID:UUID): ClientConnection?{
        return if (gameMap.containsKey(gameUUID) &&
            (gameMap[gameUUID]!!.isConnectionInGame(userUUID) )) {
            gameMap[gameUUID]!!.getClientConnection(userUUID)
        } else {
            for (gameCode:GameCode in gameCodeMap.values){
                if (gameCode.gameCodeUUID == gameUUID && gameCode.creator.id == userUUID){
                    return gameCode.creator
                }
            }
            null
        }
    }

    // Convert game ID to a easier to read game code
    // Should be random enough, but could/should implement check to see if exists
    private fun generateGameCode(gameCodeUUID: UUID): String {
        var indexModifier = 0;
        var code = hashAndExtractCode(gameCodeUUID.toString(), indexModifier)

        // Max length of sha256 string is 64 characters
        while (gameCodeMap.containsKey(code) && indexModifier < 52) {
            indexModifier++
            code = hashAndExtractCode(gameCodeUUID.toString(), indexModifier)
        }

        return code
    }

    private fun hashAndExtractCode(id: String, mod: Int): String {
        return MessageDigest
            .getInstance("SHA-256")
            .digest(id.toByteArray())
            .joinToString("") { "%02x".format(it) }
            .substring(0 + mod, 11 + mod)
            .uppercase()
    }

    // Checks if the inserted integer code is valid, based on the timestamp being not more than
    // 15 minutes old and if it is in the set of valid codes.
    // If so, it returns a valid GameCode instance.
    private fun getGameCode(gameCodeUUID: String): GameCode? {
        // This gameCode may be null if the gameCode does not exist in the set of valid gameCodes
        val gameCodeMapEntry: GameCode? = gameCodeMap[gameCodeUUID]
        if (gameCodeMapEntry != null) {
            return if (
            // Checks if the gameCodeMapEntry is no less than 15 minutes old
                getTimeMillis() - gameCodeMapEntry.timeOfCreation < gameCodeTimeoutMillis
            ) {
                gameCodeMapEntry
            } else {
                // If this returns null, then the gameCodeMapEntry has been created later than 15 minutes and therefore is not valid.
                // Thus the code must also be removed as it should not be in the map and the method returns null.
                gameCodeMap.remove(gameCodeUUID)
                null
            }
        } else {
            // If the gameCodeMapEntry is null, it does not exist in the gameCodeMap and is therefore not valid.
            return null
        }
    }

    // If the game is not created, then the second client has not joined yet.
    fun areGameSlotsFilled(gameUUID: UUID):Boolean{
        return gameMap.containsKey(gameUUID)
    }

    fun getClientConnectionsFromGame(gameUUID: UUID): Pair<ClientConnection,ClientConnection>{
        return gameMap[gameUUID]!!.gameConnections
    }

    private val gameCodeGarbageCollector =
        GameCodeGarbageCollector(gameCodeMap, gameCodeTimeoutMillis, 30000L)
}
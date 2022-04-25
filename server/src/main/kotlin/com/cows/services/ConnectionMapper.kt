package com.cows.services

import io.ktor.util.date.*
import java.util.*

// This object will map connections to the game id's
// TODO: Create serializer and verification for the received instructionlists to prevent a user from making moves for the other
// TODO: Create an efficient way of checking if a user requesting a gameCode is in an active game.
// This object compares if the incoming connection is valid for the requested game and if so,
// maps the requested instruction list to the incoming game.
object ConnectionMapper {

    // 900 000 seconds is 15 minutes
    private val gameCodeTimeoutMillis = 900000

    // we have a 5 digit code, which makes 100 000 games. this is 95% full
    private val maxActiveGames = 95000

    private var gameMap: MutableMap<UUID, Game> = Collections.synchronizedMap(mutableMapOf())

    fun createGame(clientConnection: ClientConnection, gameJoinCode: String): UUID? {
        val gameCode = getGameCodeIfValid(gameJoinCode) ?: return null
        gameMap[gameCode.gameCodeUUID] = Game(
            Pair(gameCode.creator, clientConnection),
            Pair(PlayerState(), PlayerState()))
        return gameCode.gameCodeUUID
    }

    private var gameCodeMap: MutableMap<String, GameCode> =
        Collections.synchronizedMap(mutableMapOf())

    fun isServerOverloaded(): Boolean {
        return gameMap.size >= maxActiveGames
    }

    fun createGameCode(clientConnection: ClientConnection): GameCode {
        val gameCodeUUID = UUID.randomUUID()
        val gameJoinCode = generateValidGameCode()
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

    private fun generateValidGameCode(): String {
        var code = generateCode()

        while (gameCodeIsInvalid(code)) {
            code = generateCode()
        }

        return code
    }

    private fun generateCode(): String {
        val chars = ('0'..'9')
        return (1..GameCode.CODE_LENGTH).map { chars.random() }.joinToString("")
    }

    private fun gameCodeIsInvalid(gameCode: String) = gameCodeMap.containsKey(gameCode)

    private fun getGameCodeIfValid(gameCodeUUID: String): GameCode? {
        val gameCodeMapEntry = gameCodeMap[gameCodeUUID] ?: return null

        if (gameCodeIsExpired(gameCodeMapEntry)) {
            gameCodeMap.remove(gameCodeUUID)
            return null
        }
        return gameCodeMapEntry
    }

    private fun gameCodeIsExpired(gameCode: GameCode): Boolean =
        getTimeMillis() - gameCode.timeOfCreation > gameCodeTimeoutMillis

    fun areGameSlotsFilled(gameUUID: UUID): Boolean = gameMap.containsKey(gameUUID)

    fun getClientConnectionsFromGame(gameUUID: UUID): Pair<ClientConnection,ClientConnection> =
        gameMap[gameUUID]!!.gameConnections

    fun getGameFromUUID(gameUUID: UUID) = gameMap[gameUUID]

    private val gameCodeGarbageCollector =
        GameCodeGarbageCollector(gameCodeMap, gameCodeTimeoutMillis, 30000L)
}
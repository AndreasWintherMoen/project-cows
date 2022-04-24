package com.cows.services

import com.cows.map.Coordinate
import com.cows.services.simulation.API
import com.cows.services.simulation.models.json.JsonRoundSimulation
import projectcows.rawJsonData.JsonTower
import com.cows.map.Map
import projectcows.rawJsonData.JsonUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.*
import kotlin.collections.ArrayList

class Game(
    val gameConnections: Pair<ClientConnection, ClientConnection>,
    private val playerStates: Pair<PlayerState, PlayerState>) {
    private var attackInstructions: List<JsonUnit>? = null
    private var defendInstructions: List<JsonTower>? = null
    private var roundCounter = 0

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

    fun getOtherClientConnection(userUUID: UUID) : ClientConnection {
        return if (gameConnections.first.id == userUUID) gameConnections.second else gameConnections.first
    }

    fun isPlayerAttacker(userUUID: UUID): Boolean {
        val isPlayerFirst = gameConnections.first.id == userUUID
        val isFirstPlayerAttacker = roundCounter % 2 == 0

        return !isPlayerFirst.xor(isFirstPlayerAttacker)
    }

    suspend fun addAttackInstructions(unitList: List<JsonUnit>): JsonRoundSimulation? {
        attackInstructions = unitList
        if (defendInstructions != null) {
            return simulateRound()
        } else {
            return null
        }
    }

    suspend fun addDefendInstructions(towerList: List<JsonTower>): JsonRoundSimulation? {
        defendInstructions = towerList
        if (attackInstructions != null) {
            return simulateRound()
        } else {
            return null
        }
    }

    private suspend fun simulateRound(): JsonRoundSimulation {
        println("Simulating round!")
        return API.simulate(defendInstructions!!, attackInstructions!!, Map.getPathCoordinates())
    }

    private val Id:Int = lastId.getAndIncrement()
}
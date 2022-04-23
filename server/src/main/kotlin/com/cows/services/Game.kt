package com.cows.services

import com.cows.services.simulation.API
import com.cows.services.simulation.models.json.JsonRoundSimulation
import projectcows.rawJsonData.JsonTower
import projectcows.rawJsonData.JsonUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.*


class Game(
    val gameConnections: Pair<ClientConnection, ClientConnection>,
    private val playerStates: Pair<PlayerState, PlayerState>) {
    private val path: List<IntArray> = _TEMP_generatePath()
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
//        val mappedDefenseInstructions = defendInstructions!!.mapIndexed { index, tower -> UnitStatsMapper.addStatsToJsonTower(index, tower) }
//        val mappedAttackInstructions = attackInstructions!!.mapIndexed { index, unit -> UnitStatsMapper.addStatsToJsonUnit(index, unit) }
        val roundSimulation = API.simulate(defendInstructions!!, attackInstructions!!, path)
        println("Received round simulation")
        return roundSimulation
    }

    private val Id:Int = lastId.getAndIncrement()





    private fun _TEMP_generatePath() = arrayListOf(
        intArrayOf(0, 5),
        intArrayOf(1, 5),
        intArrayOf(2, 5),
        intArrayOf(3, 5),
        intArrayOf(4, 5),
        intArrayOf(4, 6),
        intArrayOf(4, 7),
        intArrayOf(4, 8),
        intArrayOf(5, 8),
        intArrayOf(6, 8),
        intArrayOf(7, 8),
        intArrayOf(7, 7),
        intArrayOf(7, 6),
        intArrayOf(7, 5),
        intArrayOf(8, 5),
        intArrayOf(9, 5),
        intArrayOf(10, 5),
        intArrayOf(11, 5),
        intArrayOf(12, 5),
    )
}
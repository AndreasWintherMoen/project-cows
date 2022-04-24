package com.cows.services

import com.cows.services.simulation.API
import com.cows.services.simulation.models.json.JsonAvailableTowers
import com.cows.services.simulation.models.json.JsonAvailableUnits
import com.cows.services.simulation.models.json.JsonRoundSimulation
import projectcows.rawJsonData.JsonTower
import projectcows.rawJsonData.JsonUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.*


class Game(
    val gameConnections: Pair<ClientConnection, ClientConnection>,
    playerStates: Pair<PlayerState, PlayerState>) {
    val gameState = GameState(playerStates, _TEMP_generatePath())
    private val firstPlayerIsAttacker
        get() = gameState.roundCounter % 2 == 0

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
        val isFirstPlayerAttacker = gameState.roundCounter % 2 == 0

        return !isPlayerFirst.xor(isFirstPlayerAttacker)
    }

    suspend fun addAttackInstructions(unitList: List<JsonUnit>): JsonRoundSimulation? {
        gameState.attackInstructions = unitList
        gameState.playerStates.first.coins -= unitList.size
        if (gameState.defendInstructions != null) {
            val roundSimulation = simulateRound()
            nextRound(roundSimulation)
            return roundSimulation
        } else {
            return null
        }
    }

    suspend fun addDefendInstructions(towerList: List<JsonTower>): JsonRoundSimulation? {
        gameState.defendInstructions = towerList
        gameState.playerStates.first.coins -= towerList.size * 3
        if (gameState.attackInstructions != null) {
            val roundSimulation = simulateRound()
            nextRound(roundSimulation)
            return roundSimulation
        } else {
            return null
        }
    }

    private suspend fun simulateRound(): JsonRoundSimulation {
        println("Simulating round!")
        val roundSimulation = API.simulate(gameState.defendInstructions!!, gameState.attackInstructions!!, gameState.path)
        return roundSimulation
    }

    private suspend fun nextRound(roundSimulation: JsonRoundSimulation) {
        val firstPlayerWon = !firstPlayerIsAttacker.xor(roundSimulation.attackerWon)
        if (firstPlayerWon) gameState.playerStates.first.health--
        else gameState.playerStates.second.health--

        generateAvailableUnitsAndTowers()

        gameState.roundCounter++

        gameState.path = _TEMP_generatePath()
    }

    suspend fun generateAvailableUnitsAndTowers() {
        gameState.availableUnits = API.getUnitStats((0..2).random(), (0..2).random(), (0..2).random())
        gameState.availableTowers = API.getTowerStats((0..2).random(), (0..2).random(), (0..2).random())
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
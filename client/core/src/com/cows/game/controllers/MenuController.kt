package com.cows.game.controllers

import com.cows.game.Redux
import com.cows.game.enums.GameState
import com.cows.game.hud.CreateGameMenu
import com.cows.game.hud.JoinGameMenu
import com.cows.game.hud.StartMenu
import com.cows.game.managers.GameStateManager
import com.cows.game.serverConnection.ServerConnection
import kotlinx.coroutines.*

class MenuController {
    private var startMenu:StartMenu? = null
    private var joinMenu:JoinGameMenu? = null
    private var createMenu:CreateGameMenu? = null
    private var startGameJob: Job? = null

    init {
        showStartMenu()
    }

    private fun showJoinGameMenu() {
        startMenu?.die()
        startMenu = null
        joinMenu = JoinGameMenu({ code -> joinGame(code) }, { showStartMenu() } )
    }

    private fun showStartMenu() {
        joinMenu?.die()
        joinMenu = null
        createMenu?.die()
        createMenu = null
        startMenu = StartMenu({ showJoinGameMenu() }, { onCreateGameButton() })
    }

    private fun showCreateGameMenu() {
        startMenu?.die()
        startMenu = null
    }

    private fun onCreateGameButton() {
        showCreateGameMenu()
        startGameJob?.cancel()
        startGameJob = GlobalScope.launch(Dispatchers.IO) {
            val joinCode = ServerConnection.createGame()
            createMenu?.setGameCode(joinCode)
            ServerConnection.connectToActiveGame()
            Redux.jsonAvailableUnits = ServerConnection.getAvailableUnits()
            GameStateManager.setGameStateAsync(GameState.PLANNING_ATTACK)
        }
        createMenu = CreateGameMenu({showStartMenu()})
    }

    fun die() {
        joinMenu?.die()
        createMenu?.die()
        startMenu?.die()
    }

    private fun joinGame(joinCode: String) {
        runBlocking {
            ServerConnection.joinGame(joinCode)
            Redux.jsonAvailableTowers = ServerConnection.getAvailableTowers()
            GameStateManager.setGameStateAsync(GameState.PLANNING_DEFENSE)
        }
    }

}
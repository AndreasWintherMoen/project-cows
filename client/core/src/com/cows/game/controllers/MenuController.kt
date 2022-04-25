package com.cows.game.controllers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.cows.game.enums.GameState
import com.cows.game.hud.*
import com.cows.game.managers.GameStateManager
import com.cows.game.managers.RoundManager
import com.cows.game.serverConnection.ServerConnection
import io.ktor.client.features.*
import kotlinx.coroutines.*

class MenuController {
    private var startMenu:StartMenu? = null
    private var joinMenu:JoinGameMenu? = null
    private var createMenu:CreateGameMenu? = null
    private var startGameJob: Job? = null
    private var userResponse: FontObject? = null

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
            RoundManager.gameStatus = ServerConnection.getGameStatus()
            println(RoundManager.gameStatus)
            GameStateManager.setGameStateAsync(GameState.PLANNING_ATTACK)
        }
        createMenu = CreateGameMenu({showStartMenu()})
    }

    fun die() {
        joinMenu?.die()
        joinMenu = null
        createMenu?.die()
        createMenu = null
        startMenu?.die()
        startMenu = null
    }

    private fun joinGame(joinCode: String) {
        try {
            runBlocking {
              ServerConnection.joinGame(joinCode)
              RoundManager.gameStatus = ServerConnection.getGameStatus()
              println(RoundManager.gameStatus)
              GameStateManager.setGameStateAsync(GameState.PLANNING_DEFENSE)
            }
            userResponse?.dispose()
        }
        catch (error: ClientRequestException) {
            System.err.println("Wrong code")
            println(error)
            userResponse = FontObject("Wrong code", 64, Color(0.9f, 0.04f, 0f, 1f))
            userResponse!!.position = Vector2((Gdx.graphics.width/2 - userResponse!!.getFontWidth()/2),125f)
        }
    }

}
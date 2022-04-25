package com.cows.game.controllers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.cows.game.Redux
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
            try {
                val joinCode = ServerConnection.createGame()
                createMenu?.setGameCode(joinCode)
                ServerConnection.connectToActiveGame()
                RoundManager.gameStatus = ServerConnection.getGameStatus()
                println(RoundManager.gameStatus)
                GameStateManager.setGameStateAsync(GameState.PLANNING_ATTACK)
                Redux.playerCreatedGame = true
            } catch (error: ClientRequestException) {
                System.err.println("Error creating game")
                println(error)
                Redux.errorMessage = error.message
            }
        }
        createMenu = CreateGameMenu({showStartMenu()})
    }

    private fun joinGame(joinCode: String) {
        GlobalScope.launch(Dispatchers.IO) {
              try {
                  userResponse?.die()
                  userResponse = null
                  ServerConnection.joinGame(joinCode)
                  RoundManager.gameStatus = ServerConnection.getGameStatus()
                  println(RoundManager.gameStatus)
                  GameStateManager.setGameStateAsync(GameState.PLANNING_DEFENSE)
                  Redux.playerCreatedGame = false
              } catch (error: ClientRequestException) {
                  System.err.println("Wrong code")
                  println(error)
                  Redux.errorMessage = "Wrong code"
              }
        }
        userResponse?.dispose()
    }

    fun showError(message: String) {
        userResponse = FontObject(message, 64, Color(0.9f, 0.04f, 0f, 1f))
        userResponse!!.position = Vector2((Gdx.graphics.width/2 - userResponse!!.getFontWidth()/2),125f)
        joinMenu?.onWrongCode()
    }

    fun die() {
        joinMenu?.die()
        joinMenu = null
        createMenu?.die()
        createMenu = null
        startMenu?.die()
        startMenu = null
    }


}
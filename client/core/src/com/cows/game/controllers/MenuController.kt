package com.cows.game.controllers

import com.cows.game.enums.GameState
import com.cows.game.hud.CreateGameMenu
import com.cows.game.hud.JoinGameMenu
import com.cows.game.hud.StartMenu
import com.cows.game.managers.FunctionDelayer
import com.cows.game.managers.GameStateManager
import com.cows.game.serverConnection.ServerConnection
import kotlinx.coroutines.runBlocking

class MenuController {
    private var startMenu:StartMenu? = null
    private var joinMenu:JoinGameMenu? = null
    private var createMenu:CreateGameMenu? = null

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
        startMenu = StartMenu({ showJoinGameMenu() }, { showCreateGameMenu() })
    }

    private fun showCreateGameMenu() {
        startMenu?.die()
        startMenu = null
        var joinCode: String? = null
        runBlocking {
            joinCode = ServerConnection.createGame()
            println("joinCode: $joinCode")
            println(joinCode!!.map { it.digitToInt() })
            createMenu = CreateGameMenu(joinCode!!, {showStartMenu()})
        }
        FunctionDelayer.invokeFunctionAtEndOfNextFrame {
            FunctionDelayer.invokeFunctionAtEndOfNextFrame {
                runBlocking {
                    ServerConnection.connectToActiveGame()
                    GameStateManager.currentGameState = GameState.PLANNING_ATTACK
                }
            }

        }

    }

    fun die() {
        joinMenu?.die()
        createMenu?.die()
        startMenu?.die()
    }

    private fun joinGame(joinCode: String) {
        runBlocking {
            ServerConnection.joinGame(joinCode)

            GameStateManager.currentGameState = GameState.PLANNING_DEFENSE
        }
    }

}
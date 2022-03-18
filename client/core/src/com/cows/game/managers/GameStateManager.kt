package com.cows.game.managers

import com.cows.game.enums.GameState
import com.cows.game.gameState.GameStateSubscriber
import java.util.*



object GameStateManager {
    var gameStateSubscribers = mutableListOf<GameStateSubscriber>()
    var currentGameState = GameState.PLANNING_DEFENSE
        set(newGameState) {
            if (!isValidGameState(newGameState)) return
            onChangeGameStateChange(field, newGameState)
            field = newGameState
        }

    fun addSubscriber(subscriber: GameStateSubscriber){
        gameStateSubscribers.add(subscriber)
    }

    fun removeSubscriber(subscriber: GameStateSubscriber){
        gameStateSubscribers.remove(subscriber)
    }

    fun isValidGameState(newGameState: GameState): Boolean{
        if(newGameState == currentGameState) return false
        return true
    }

    private fun onChangeGameStateChange(oldGameState: GameState, newGameState: GameState) {
        for (subscriber in gameStateSubscribers) {
            subscriber.onChangeGameState(oldGameState, newGameState)
        }
    }
}
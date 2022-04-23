package com.cows.game.managers

import com.cows.game.enums.GameState
import com.cows.game.gameState.GameStateSubscriber
import java.util.*



object GameStateManager {
    var gameStateSubscribers = mutableListOf<GameStateSubscriber>()
    var currentGameState = GameState.NONE
        set(newGameState) {
            if (!isValidGameState(newGameState)) return
            onChangeGameStateChange(field, newGameState)
            field = newGameState
        }
    var nextAsyncGameState: GameState? = null

    fun setGameStateAsync(newGameState: GameState) {
        nextAsyncGameState = newGameState
    }

    fun addSubscriber(subscriber: GameStateSubscriber){
        gameStateSubscribers.add(subscriber)
    }

    fun removeSubscriber(subscriber: GameStateSubscriber){
        gameStateSubscribers.remove(subscriber)
    }

    fun isValidGameState(newGameState: GameState): Boolean{
        if(newGameState == currentGameState) return false
        //TODO: Add more logic here.
        return true
    }

    private fun onChangeGameStateChange(oldGameState: GameState, newGameState: GameState) {
        for (subscriber in gameStateSubscribers) {
            subscriber.onChangeGameState(oldGameState, newGameState)
        }
    }
}
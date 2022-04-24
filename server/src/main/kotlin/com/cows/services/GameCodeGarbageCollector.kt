package com.cows.services

import io.ktor.util.date.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class GameCodeGarbageCollector(private val gameCodeMap:MutableMap<String,GameCode>,private val gameCodeTimeout: Int, private val timeBetweenChecks:Long) {

    init {
        cleanGameCodeMap()
    }

    fun cleanGameCodeMap() {
         GlobalScope.launch {
            while(true){
                delay(timeBetweenChecks)
                removeOutdatedGameCodes()
            }
        }
    }

    // Function which takes in a reference to a gameCodeMap and removes the outdatedGameCodes
    // Mostly a convenience function to make the concurrent loop more readable
    private fun removeOutdatedGameCodes(){
        val currentTimestamp = getTimeMillis()
        gameCodeMap
            .filter { (currentTimestamp-it.value.timeOfCreation < gameCodeTimeout) }
            .forEach{ gameCodeMap.remove(it.key) }
        }
    }
package com.cows.services

import java.util.concurrent.atomic.AtomicInteger
import java.util.*

class Game() {
    companion object {
        // TODO: Implement a UUID-system instead of a counter
        var lastId = AtomicInteger(0)
    }

    private val Id:Int = lastId.getAndIncrement()

    // TODO: Implement check for other current codes
    // This is the initial code created by a user
    private val gameCode:GameCode = GameCode()





}
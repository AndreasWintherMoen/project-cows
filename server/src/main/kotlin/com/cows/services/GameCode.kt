package com.cows.services

import io.ktor.util.date.*

class GameCode(val gameCodeId:Int) {
    val timeOfCreation:Long = getTimeMillis()
}
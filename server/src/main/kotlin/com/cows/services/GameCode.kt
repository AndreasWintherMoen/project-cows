package com.cows.services

import io.ktor.util.date.*

class GameCode() {
    val timeOfCreation: Long = getTimeMillis()
    val gameCode: Int = (0..99999).random()
}

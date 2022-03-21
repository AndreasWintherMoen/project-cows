package com.cows.services

import io.ktor.util.date.*

class GameCode(val gameCodeId:Int,val creator:ClientConnection ) {
    val timeOfCreation:Long = getTimeMillis()
}
package com.cows.services

import io.ktor.util.date.*
import java.util.*

class GameCode(val gameJoinCode:String, val gameCodeUUID: UUID, val creator:ClientConnection ) {
    val timeOfCreation:Long = getTimeMillis()
}

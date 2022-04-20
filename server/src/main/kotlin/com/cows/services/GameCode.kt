package com.cows.services

import io.ktor.util.date.*
import java.util.*

class GameCode(val gameJoinCode:String, val gameCodeUUID: UUID, val creator:ClientConnection ) {
    companion object {
        const val CODE_LENGTH = 5
    }
    val timeOfCreation:Long = getTimeMillis()
}

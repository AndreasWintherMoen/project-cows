package com.cows.game.serverConnection.shared

import java.util.*

data class GameCreateResponse(
    val userId: UUID,
    val gameJoinCode: String,
    val gameCodeUUID: UUID,
)

data class GameJoinResponse(
    val userId: UUID,
    val gameCodeUUID: UUID,
)
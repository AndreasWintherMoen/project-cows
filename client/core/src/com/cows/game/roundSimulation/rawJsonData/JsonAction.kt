package com.cows.game.roundSimulation.rawJsonData

import com.cows.game.roundSimulation.ActionType

data class JsonAction (
    val subject: Int,
    val verb: ActionType,
    val obj: Int?
)


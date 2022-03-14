package com.cows.game.roundSimulation

import com.cows.game.Coordinate

enum class ActionTypes {
    TARGET,
    ATTACK,
    MOVE,
    DIE,
    WIN,
    SPAWN
}

data class Action (
    val subject: Int,
    val verb: String,
    val obj: Coordinate
 )
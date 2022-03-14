package com.cows.game.models

import com.cows.game.Coordinate

data class Tower (
    val id: Int,
    val type: String,
    val position: Coordinate
)
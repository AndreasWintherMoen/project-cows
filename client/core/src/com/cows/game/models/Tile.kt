package com.cows.game.models

import com.cows.game.Coordinate
import com.cows.game.enums.TileType

data class Tile(
    val type: TileType,
    val coordinate: Coordinate,
    ) {
    companion object {
        const val WIDTH = 64f
        const val HEIGHT = 64f
    }
}
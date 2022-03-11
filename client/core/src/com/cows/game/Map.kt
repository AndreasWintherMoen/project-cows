package com.cows.game

import com.cows.game.controllers.TileController
import com.cows.game.enums.TileType

class Map {
    companion object {
        const val WIDTH = 16
        const val HEIGHT = 8
        //temporary
        val PATH = arrayListOf(
            Coordinate(15, 5),
            Coordinate(14, 5),
            Coordinate(13, 5),
            Coordinate(13, 6),
            Coordinate(13, 7),
            Coordinate(12, 7),
            Coordinate(11, 7),
            Coordinate(10, 7),
            Coordinate(10, 6),
            Coordinate(10, 5),
            Coordinate(10, 4),
            Coordinate(10, 3),
            Coordinate(9, 3),
            Coordinate(8, 3),
            Coordinate(7, 3),
            Coordinate(6, 3),
            Coordinate(5, 3),
            Coordinate(5, 4),
            Coordinate(5, 5),
            Coordinate(4, 5),
            Coordinate(3, 5),
            Coordinate(2, 5),
            Coordinate(1, 5),
            Coordinate(0, 5),
        )
    }

    private val tiles = Array(WIDTH) { x -> Array(HEIGHT) { y ->
        TileController(if (PATH.contains(Coordinate(x, y))) TileType.PATH else TileType.GRASS, Coordinate(x, y))
    } }
}
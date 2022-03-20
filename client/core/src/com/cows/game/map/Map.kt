package com.cows.game.map

import com.badlogic.gdx.math.Vector2
import com.cows.game.controllers.TileController
import com.cows.game.enums.TileType
import com.cows.game.models.TileModel

object Map {

    const val WIDTH = 16
    const val HEIGHT = 8

    //temporary
    // TODO: Implement a map parser and load path from file
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

    fun init() {}

    private val tiles = Array(WIDTH) { x -> Array(HEIGHT) { y ->
        TileController(if (PATH.contains(Coordinate(x, y))) TileType.PATH else TileType.GRASS, Coordinate(x, y))
    } }

    fun getTile(x: Int, y: Int): TileController {
        if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT) throw Error("Tile ($x, $y) out of bounds")
        return tiles[x][y]
    }

    fun getTileAtPathIndex(index: Int): TileController {
        if (index < 0 || index >= PATH.size) throw Error("Path index $index out of bounds")
        val coordinate = PATH[index]
        return getTile(coordinate.x, coordinate.y)
    }

    fun getTileAtPixel(pixel: Vector2): TileController {
        val x = (pixel.x / TileModel.WIDTH).toInt()
        val y = (pixel.y / TileModel.HEIGHT).toInt()
        return getTile(x, y)
    }
}
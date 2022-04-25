package com.cows.game.map

import com.badlogic.gdx.math.Vector2
import com.cows.game.controllers.TileController
import com.cows.game.enums.TileType
import com.cows.game.managers.RoundManager
import com.cows.game.models.TileModel

object Map {

    const val WIDTH = 13
    const val HEIGHT = 8
    var PATH: List<Coordinate> = loadDefaultMap()
    private var tiles: Array<Array<TileController>>? = null

    fun init() {
        loadMap(intArrayListToCoordinateList(RoundManager.gameStatus!!.path))
    }

    private fun intArrayListToCoordinateList(intArrayList: List<IntArray>): List<Coordinate> =
        intArrayList.map { Coordinate(it[0], it[1]) }.toList()

    fun loadMap(path: List<Coordinate>){
        tiles = Array(WIDTH) { x -> Array(HEIGHT) { y ->
            TileController(if (path.contains(Coordinate(x, y))) TileType.PATH else TileType.GRASS, Coordinate(x, y))
        } }
        PATH = path
    }

    fun getTile(x: Int, y: Int): TileController {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) throw Error("Tile ($x, $y) out of bounds")
        return tiles!![x][y]
    }

    fun getTileAtPathIndex(index: Int): TileController {
        if (index < 0 || index >= PATH.size) throw Error("Path index $index out of bounds")
        val coordinate = PATH[index]
        return getTile(coordinate.x, coordinate.y)
    }

    fun getTileAtPixel(pixel: Vector2): TileController? {
        if (pixel.x > (WIDTH * TileModel.WIDTH)) {
            println(pixel.x)
            println("Outside map")
            return null
        }
        val x = (pixel.x / TileModel.WIDTH).toInt()
        val y = (pixel.y / TileModel.HEIGHT).toInt()
        println("Inside map")
        return getTile(x, y)
    }

    private fun loadDefaultMap(): ArrayList<Coordinate>{
        return arrayListOf(
            Coordinate(0, 5),
            Coordinate(1, 5),
            Coordinate(2, 5),
            Coordinate(3, 5),
            Coordinate(4, 5),
            Coordinate(4, 6),
            Coordinate(4, 7),
            Coordinate(4, 8),
            Coordinate(5, 8),
            Coordinate(6, 8),
            Coordinate(7, 8),
            Coordinate(7, 7),
            Coordinate(7, 6),
            Coordinate(7, 5),
            Coordinate(8, 5),
            Coordinate(9, 5),
            Coordinate(10, 5),
            Coordinate(11, 5),
            Coordinate(12, 5),)
    }
}
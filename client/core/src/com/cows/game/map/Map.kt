package com.cows.game.map

import com.badlogic.gdx.math.Vector2
import com.cows.game.Application
import com.cows.game.controllers.TileController
import com.cows.game.enums.TileType
import com.cows.game.hud.ActionPanel
import com.cows.game.models.TileModel
import java.io.File
import java.lang.Exception

object Map {

    const val WIDTH = 16
    const val HEIGHT = 8
    val PATH = load()
    const val FILE_NAME = "map"

    private val tiles = Array(WIDTH) { x -> Array(HEIGHT) { y ->
        TileController(if (PATH.contains(Coordinate(x, y))) TileType.PATH else TileType.GRASS, Coordinate(x, y))
    } }

    fun init() {}

    private fun load(): ArrayList<Coordinate> {
        return try {
            val charMap = File("maps/$FILE_NAME.map").readText().lines().map { it.toCharArray() }
            val map = charMap.map { charArrayToIntArray(it) }
            generatePath(map)
        }
        catch (e: Exception){
            error("""
                Map file is not in correct format.
                Correct format should be a 16x8 table of numbers [1: Path, 0: Grass] (Might add more) 
                Ex.
                0000000000000000
                0000000011110000
                0000000110011100
                0000000100000100
                0000000100000111
                0000000100000000
                1111111100000000
                0000000000000000
            """.trimMargin())
        }
    }

    private fun charArrayToIntArray(array: CharArray) = array.map { it.digitToInt() }

    private fun generatePath(map: List<List<Int>>): ArrayList<Coordinate>{
        val tempPath = arrayListOf<Coordinate>();

        for(w in 0 until WIDTH){
            val tempCoordinates = arrayListOf<Coordinate>();
            for(h in 0 until HEIGHT){
                if(map[h][w] == 1){
                    tempCoordinates.add(Coordinate(w, HEIGHT-1-h))
                }
            }
            if(tempCoordinates.size > 1 && tempPath.size > 1 && tempPath.last().y != tempCoordinates[0].y){
                tempCoordinates.reverse()
            }
            tempPath += tempCoordinates
        }
        return tempPath
    }

    fun getPathCoordinates(): List<IntArray> {
        return PATH.map { c -> intArrayOf(c.x, c.y) }
    }

    fun flipPath(): List<Coordinate> {
        return PATH.map { Coordinate(15 - it.x, it.y) }
    }

    fun getTile(x: Int, y: Int): TileController {
        if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT) throw Error("Tile ($x, $y) out of bounds")
        return tiles[x][y]
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
}
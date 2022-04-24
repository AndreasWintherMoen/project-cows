package com.cows.map

import java.io.File
import java.lang.Exception

object Map {
    const val WIDTH = 16
    const val HEIGHT = 8
    val PATH = load()
    const val FILE_NAME = "map"

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
                    tempCoordinates.add(Coordinate(w, HEIGHT -1-h))
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
}
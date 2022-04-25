package com.cows.map

import java.io.File

class Map {
    val WIDTH = 13
    val HEIGHT = 8
    val PATH = load()

    private fun load(): ArrayList<Coordinate> {
        val fileNames = getMapFiles()
        val randomMapFile = if(fileNames.size > 0) fileNames.random() else ""

        while (fileNames.size > 0) {
            val charMap = File("maps/$randomMapFile").readText().lines().map { it.toCharArray() }
            val map = charMap.map { charArrayToIntArray(it) }
            val path = generatePath(map, randomMapFile)
            if(path == null) fileNames.remove(randomMapFile) else return path
        }
        return loadDefaultMap()
    }

    private fun charArrayToIntArray(array: CharArray) = array.map { it.digitToInt() }

    private fun generatePath(map: List<List<Int>>, pathName: String): ArrayList<Coordinate>? {
        val tempPath = arrayListOf<Coordinate>();

        try {
            for (w in 0 until WIDTH) {
                val tempCoordinates = arrayListOf<Coordinate>();
                for (h in 0 until HEIGHT) {
                    if (map[h][w] == 1) {
                        tempCoordinates.add(Coordinate(w, HEIGHT - 1 - h))
                    }
                }
                if (tempCoordinates.size > 1 && tempPath.size > 1 && tempPath.last().y != tempCoordinates[0].y) {
                    tempCoordinates.reverse()
                }
                tempPath += tempCoordinates
            }
            return tempPath
        }
        catch (e: Exception){
            println(
                """
                Map file, $pathName, is not in correct format.
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
            """.trimMargin()
            )
            return null
        }
    }

    fun getPathCoordinates(): List<IntArray> {
        return PATH.map { c -> intArrayOf(c.x, c.y) }
    }

    fun getMapFiles(): ArrayList<String> {
        val fileNames = arrayListOf<String>()
        File("maps/").walkBottomUp().forEach { if(it.name.endsWith(".map", true)) fileNames.add(it.name) }
        return fileNames
    }

    // Just in case none of the provided map files work
    private fun loadDefaultMap(): ArrayList<Coordinate>{
        return arrayListOf(
            Coordinate(0, 5),
            Coordinate(1, 5),
            Coordinate(2, 5),
            Coordinate(3, 5),
            Coordinate(4, 5),
            Coordinate(4, 6),
            Coordinate(4, 6),
            Coordinate(4, 6),
            Coordinate(5, 6),
            Coordinate(6, 6),
            Coordinate(7, 6),
            Coordinate(7, 6),
            Coordinate(7, 6),
            Coordinate(7, 5),
            Coordinate(8, 5),
            Coordinate(9, 5),
            Coordinate(10, 5),
            Coordinate(11, 5),
            Coordinate(12, 5),)
    }
}
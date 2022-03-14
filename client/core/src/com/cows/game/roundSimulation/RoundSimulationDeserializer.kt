package com.cows.game.roundSimulation

import com.google.gson.Gson

class RoundSimulationDeserializer {
    companion object {
        fun deserialize(rawJsonRoundSimulation: String): RoundSimulation {
//            println("deserializing round sim from json. Input: $rawJsonRoundSimulation")
            val gson = Gson()
            return gson.fromJson(rawJsonRoundSimulation, RoundSimulation::class.java)
        };
    }
}



//0000000000000000
//0000000000000000
//0000000000000000
//0000000000000000
//0000000000000000
//0000000000000000
//0000000011110000
//0000000110011100
//0000000100000100
//0000000100000111
//0000000100000000
//1111111100000000
//0000000000000000
//0000000000000000
//0000000000000000
//0000000000000000



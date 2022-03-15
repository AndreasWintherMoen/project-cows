package com.cows.game.roundSimulation

import com.cows.game.roundSimulation.rawJsonData.JsonRoundSimulation
import com.google.gson.Gson

class RoundSimulationDeserializer {
    companion object {
        fun deserialize(rawJsonRoundSimulation: String): JsonRoundSimulation {
            val gson = Gson()
            return gson.fromJson(rawJsonRoundSimulation, JsonRoundSimulation::class.java)
        }
    }
}

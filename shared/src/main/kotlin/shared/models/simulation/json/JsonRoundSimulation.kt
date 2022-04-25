package models.simulation.json

data class JsonRoundSimulation (
    val towerList: List<JsonTower>,
    val unitList: List<JsonUnit>,
    val eventLog: List<JsonTick>,
    val attackerWon: Boolean
)


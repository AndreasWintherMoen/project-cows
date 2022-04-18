package projectcows.rawJsonData

data class JsonRoundSimulation (
    val towerList: List<JsonTower>,
    val unitList: List<JsonUnit>,
    val eventLog: List<JsonTick>
)


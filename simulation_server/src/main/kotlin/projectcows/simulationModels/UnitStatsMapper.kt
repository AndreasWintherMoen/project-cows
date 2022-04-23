package projectcows.simulationModels

import projectcows.enums.UnitType
import projectcows.models.Coordinate
import projectcows.rawJsonData.JsonTower
import projectcows.rawJsonData.JsonUnit

object UnitStatsMapper {
    fun getTowerRange(type: UnitType, level: Int): Int {
        when (type) {
            UnitType.NONE -> return 1
            UnitType.FIRE -> return when (level) {
                0 -> 2
                1 -> 3
                2 -> 5
                else -> 2
            }
            UnitType.WATER -> return when (level) {
                0 -> 3
                1 -> 4
                2 -> 6
                else -> 2
            }
            UnitType.GRASS -> return when (level) {
                0 -> 1
                1 -> 2
                2 -> 3
                else -> 1
            }
        }
    }

    fun getTowerTimeBetweenAttacks(type: UnitType, level: Int): Int {
        return 5
        // I've just set time between attacks to 5 (1 second) for now. I think we might just keep this because
        // animations might look weird on different fire rates, and it makes it easier to make a stats card
        // for defenders, because then we have 2 stats (damage and range), similar to units which also
        // have 2 stats (health and movement speed)
//        when (jsonTower.type) {
//            UnitType.NONE -> return 10
//            UnitType.FIRE -> return when (jsonTower.level) {
//                0 -> 10
//                1 -> 8
//                2 -> 5
//                else -> 10
//            }
//            UnitType.WATER -> return when (jsonTower.level) {
//                0 -> 15
//                1 -> 10
//                2 -> 10
//                else -> 15
//            }
//            UnitType.GRASS -> return when (jsonTower.level) {
//                0 -> 8
//                1 -> 5
//                2 -> 5
//                else -> 8
//            }
//        }
    }

    private fun getTowerDamage(type: UnitType, level: Int): Int {
        when (type) {
            UnitType.NONE -> return 10
            UnitType.FIRE -> return when (level) {
                0 -> 10
                1 -> 15
                2 -> 20
                else -> 10
            }
            UnitType.WATER -> return when (level) {
                0 -> 8
                1 -> 12
                2 -> 16
                else -> 8
            }
            UnitType.GRASS -> return when (level) {
                0 -> 15
                1 -> 20
                2 -> 25
                else -> 15
            }
        }
    }

    fun getUnitHealth(type: UnitType, level: Int): Int {
        when (type) {
            UnitType.NONE -> return 20
            UnitType.FIRE -> return when (level) {
                0 -> 20
                1 -> 40
                2 -> 80
                else -> 20
            }
            UnitType.WATER -> return when (level) {
                0 -> 30
                1 -> 50
                2 -> 99
                else -> 10
            }
            UnitType.GRASS -> return when (level) {
                0 -> 15
                1 -> 25
                2 -> 70
                else -> 15
            }
        }
    }

    fun getUnitMovementSpeed(type: UnitType, level: Int): Int {
        when (type) {
            UnitType.NONE -> return 8
            UnitType.FIRE -> return when (level) {
                0 -> 8
                1 -> 8
                2 -> 5
                else -> 8
            }
            UnitType.WATER -> return when (level) {
                0 -> 7
                1 -> 6
                2 -> 4
                else -> 7
            }
            UnitType.GRASS -> return when (level) {
                0 -> 10
                1 -> 10
                2 -> 8
                else -> 10
            }
        }
    }

    fun jsonTowerToSimulationModel(index: Int, jsonTower: JsonTower, unitPath: List<IntArray>): TowerSimulationModel = TowerSimulationModel(
        index,
        jsonTower.position,
        getTowerRange(jsonTower.type, jsonTower.level),
        unitPath,
        getTowerTimeBetweenAttacks(jsonTower.type, jsonTower.level),
        getTowerDamage(jsonTower.type, jsonTower.level)
    )

    fun jsonUnitToSimulationModel(index: Int, jsonUnit: JsonUnit): UnitSimulationModel = UnitSimulationModel(
        index + 100,
        getUnitHealth(jsonUnit.type, jsonUnit.level),
        getUnitMovementSpeed(jsonUnit.type, jsonUnit.level),
        index * 5
    )

    fun createJsonTowerWithStats(type: UnitType, level: Int) = JsonTower(
        null,
        type,
        level,
        Coordinate(-1, -1),
        getTowerRange(type, level),
        getTowerTimeBetweenAttacks(type, level),
        getTowerDamage(type, level)
    )

    fun createJsonUnitWithStats(type: UnitType, level: Int) = JsonUnit(
        null,
        type,
        level,
        getUnitMovementSpeed(type, level),
        getUnitHealth(type, level)
    )

    fun appendMissingDataToJsonTower(index: Int, tower: JsonTower) = JsonTower(
        index,
        tower.type,
        tower.level,
        tower.position,
        getTowerRange(tower.type, tower.level),
        getTowerTimeBetweenAttacks(tower.type, tower.level),
        getTowerDamage(tower.type, tower.level)
    )

    fun appendMissingDataToJsonUnit(index: Int, unit: JsonUnit) = JsonUnit (
        index + 100,
        unit.type,
        unit.level,
        getUnitMovementSpeed(unit.type, unit.level),
        getUnitHealth(unit.type, unit.level)
    )

}
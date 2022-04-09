package com.cows.game.roundSimulation.simulationModels


import com.cows.game.map.Coordinate

class TowerSimulationModel(val id: Int, val position: Coordinate, range: Int, unitPath: List<IntArray>, private val timeBetweenAttacks: Int, private val damage: Int) {
    var target: UnitSimulationModel? = null
    var cooldown: Int = 0
    var pathIndicesInRange = findPathIndicesInRange(range, unitPath)


    fun decrementCooldown() {
        if (cooldown > 0) {
            cooldown--
        }
    }

    private fun setCooldown() {
        cooldown = timeBetweenAttacks
    }

    fun attack() {
        target?.let {
            it.damage(damage)
            setCooldown()
        }
    }

    fun targetInRange(): Boolean {
        target?.let {
            if (it.isDead) return false
            return pathIndicesInRange.contains(it.pathIndex)
        }
        return false
    }


    private fun findPathIndicesInRange(range: Int, path: List<IntArray>): List<Int> = path
            .withIndex()
            .filter { inXRange(range, it.value) && inYRange(range, it.value) }
            .map { it.index }
            .reversed()

    private fun inXRange(range: Int, coordinate: IntArray) = position.x - range <= coordinate[0] && coordinate[0] <= position.x + range
    private fun inYRange(range: Int, coordinate: IntArray) = position.y - range <= coordinate[1] && coordinate[1] <= position.y + range

    fun findNewTarget(units: List<UnitSimulationModel>): UnitSimulationModel? {
        //Assumption: pathIndicesInRange are ordered in decreasing order
        //room for optimization if needed, eg by checking if the unit pos is outside of the range, and then prune it away
        pathIndicesInRange.forEach { index ->
            units.forEach { unit ->
                if (unit.pathIndex == index && !unit.isDead && unit.isSpawned) {
                    return unit
                }
            }
        }
        return null
    }
}


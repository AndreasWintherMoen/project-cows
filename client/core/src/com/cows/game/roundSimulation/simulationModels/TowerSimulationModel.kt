package com.cows.game.roundSimulation.simulationModels


import com.cows.game.map.Coordinate

class TowerSimulationModel(val id: Int, val position: Coordinate, range: Int, unitPath: List<IntArray>, private val timeBetweenAttacks: Int, private val damage: Int) {
    var target: UnitSimulationModel? = null
    var cooldown: Int = 0
    var pathIndicesInRange = findPathIndicesInRange(range, unitPath)

    fun decrementCooldown() {
        if (cooldown < 0) {
            cooldown--
        }
    }

    fun setCooldown() {
        cooldown = timeBetweenAttacks
    }

    fun attack() {
        target!!.damage(damage)
        setCooldown()
    }

    fun targetInRange(): Boolean = target != null && pathIndicesInRange.contains(target!!.pathIndex)


    fun findPathIndicesInRange(range: Int, path: List<IntArray>) = path
            .filter { inXRange(range, it) && inYRange(range, it) }
            .mapIndexed { index, _ -> index }


    private fun inXRange(range: Int, coordinate: IntArray) = position.x - range <= coordinate[0] && coordinate[0] <= position.x + range
    private fun inYRange(range: Int, coordinate: IntArray) = position.y - range <= coordinate[1] && coordinate[1] <= position.y + range

    fun findNewTarget(units: MutableList<UnitSimulationModel>): UnitSimulationModel? {
        //Assumption: pathIndicesInRange are ordered in decreasing order
        //room for optimization if needed, eg by checking if the unit pos is outside of the range, and then prune it away
        pathIndicesInRange.forEach { index ->
            units.forEach { unit ->
                if (unit.movementProgress == index) {
                    return unit
                }
            }
        }
        return null
    }
}


package com.cows.game.roundSimulation.simulationModels


class UnitSimulationModel(
        val id : Int,
        val movementSpeed : Int, //ticks required for moving one square
        var movementProgress: Int,
        var health: Int,
        var pathIndex : Int, //represents position in the Path Array.
        var timeToSpawn : Int //TODO implement. Ticks until this unit will spawn
        ) {

        fun damage(damage : Int){
                health.minus(damage)
        }
        fun isDead() : Boolean{
                return health <= 0
        }

        fun incrementMovementProgress(){
                movementProgress++
        }

        fun move(){
                this.pathIndex++
        }

        fun die() {
                TODO("Not yet implemented")
        }

        fun win() {
                TODO("Not yet implemented")
        }

}

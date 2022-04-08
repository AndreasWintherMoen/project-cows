package com.cows.game.roundSimulation.simulationModels


class UnitSimulationModel(
        val id : Int,
        val movementSpeed : Int, //ticks required for moving one square
        var movementProgress: Int,
        var health: Int,
        var pathIndex : Int, //represents position in the Path Array.
        var timeToSpawn : Int, //number of ticks before initial move for unit
        var isSpawned: Boolean = false,
        var isDead : Boolean = false
        ) {

        fun damage(damage : Int){
                health -=damage
        }
        fun resetMovementProgress(){
                movementProgress -= movementSpeed;
        }
        fun incrementMovementProgress() {
                movementProgress++
        }

        fun move(){
                this.pathIndex++
        }

        fun die() {
                //TODO("Not yet implemented")
                isDead = true
                println("unit died!")
        }

        fun win() {
                println("unit won!")
                //TODO("Not yet implemented")
        }
        fun spawn(){
                isSpawned = true;
        }
}

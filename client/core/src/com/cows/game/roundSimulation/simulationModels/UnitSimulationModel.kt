package com.cows.game.roundSimulation.simulationModels


class UnitSimulationModel(
        val id : Int,
        var health: Int,
        val movementSpeed : Int, //ticks required for moving one square
        var timeToSpawn : Int, //number of ticks before initial move for unit
        var pathIndex : Int = 0, //represents position in the Path Array.
        var movementProgress: Int = 0,
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
                isDead = true
        }

        fun win() {
                println("unit won!")
                //TODO do something more here?
        }
        fun spawn(){
                isSpawned = true;
        }
}

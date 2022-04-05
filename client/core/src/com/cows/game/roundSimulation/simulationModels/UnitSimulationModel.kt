package com.cows.game.roundSimulation.simulationModels


class UnitSimulationModel(
        val id : Int,
        val movementSpeed : Int, //ticks required for moving one square
        var movementProgress: Int,
        var health: Int,
        var pathIndex : Int, //represents position in the Path Array.
        var timeToSpawn : Int //number of ticks before initial move for unit
        ) {

        fun damage(damage : Int){
                health.minus(damage)
        }
        fun isDead() : Boolean{
                return health <= 0
        }
        fun resetMovementProgress(){
                movementProgress -= movementSpeed;
        }
        fun incrementMovementProgress(){
                if(timeToSpawn < 0){
                        movementProgress++
                }else{
                        timeToSpawn--
                }
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

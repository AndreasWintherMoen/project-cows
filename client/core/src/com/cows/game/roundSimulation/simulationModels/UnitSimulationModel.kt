package com.cows.game.roundSimulation.simulationModels

import com.cows.game.Coordinate


class UnitSimulationModel(
        val id : Int,
        val movementSpeed : Int, //ticks required for moving one square
        var movementProgress: Int,
        var health: Int,
        var position : Coordinate,
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

        fun move(newPosition : Coordinate){
                this.position = newPosition
        }

}

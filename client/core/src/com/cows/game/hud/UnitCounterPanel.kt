package com.cows.game.hud

import com.cows.game.enums.UnitType
import com.cows.game.views.Renderable

class UnitCounterPanel() {
    private val availableUnitCounter = UnitCounter(UnitType.NONE, 5)
    private val fireUnitCounter = UnitCounter(UnitType.FIRE, 0)
    private val waterUnitCounter = UnitCounter(UnitType.WATER, 0)
    private val grassUnitCounter = UnitCounter(UnitType.GRASS, 0)

    fun addUnit(type: UnitType) {
        when(type){
            UnitType.NONE -> println("Nope, sorry...")
            UnitType.FIRE -> if (availableUnitCounter.count > 0) {
                fireUnitCounter.count += 1
                availableUnitCounter.count -= 1
            }
            UnitType.WATER -> if (availableUnitCounter.count > 0) {
                waterUnitCounter.count += 1
                availableUnitCounter.count -= 1
            }
            UnitType.GRASS -> if (availableUnitCounter.count > 0) {
                grassUnitCounter.count += 1
                availableUnitCounter.count -= 1
            }
        }
    }

    fun removeUnit(type:UnitType) {
        when(type){
            UnitType.NONE -> println("Do you even want to win?")
            UnitType.FIRE -> if (fireUnitCounter.count > 0) {
                fireUnitCounter.count -= 1
            }
            UnitType.WATER -> if (waterUnitCounter.count > 0) {
                waterUnitCounter.count -= 1
            }
            UnitType.GRASS -> if (grassUnitCounter.count > 0) {
                grassUnitCounter.count -= 1
            }
        }
    }
}
package com.cows.game.hud

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.cows.game.Application
import com.cows.game.enums.UnitType
import com.cows.game.views.Renderable

class UnitCounterPanel(availableUnits:Int):Renderable(){
    private val availableUnitCounter = UnitCounter(UnitType.NONE, availableUnits)
    private val fireUnitCounter = UnitCounter(UnitType.FIRE, 0)
    private val waterUnitCounter = UnitCounter(UnitType.WATER, 0)
    private val grassUnitCounter = UnitCounter(UnitType.GRASS, 0)
    val font = BitmapFont()

    fun hasAvailableUnits():Boolean {
        return availableUnitCounter.count > 0
    }

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
                availableUnitCounter.count += 1
            }
            UnitType.WATER -> if (waterUnitCounter.count > 0) {
                waterUnitCounter.count -= 1
                availableUnitCounter.count += 1
            }
            UnitType.GRASS -> if (grassUnitCounter.count > 0) {
                grassUnitCounter.count -= 1
                availableUnitCounter.count += 1
            }
        }
    }

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        val appWidth = Application.WIDTH
        val appHeight = Application.HEIGHT
        val panelWidth = ActionPanel.WIDTH

        // This could be simplified if we put all UnitCounters in a list, and just loop through them
        batch.draw(availableUnitCounter.iconTexture, appWidth - panelWidth + panelWidth/4*0, appHeight-64f)
        font.draw(batch, availableUnitCounter.count.toString(), appWidth - panelWidth + panelWidth/4*0, appHeight-64f);
        batch.draw(fireUnitCounter.iconTexture, appWidth - panelWidth + panelWidth/4*1, appHeight-64f)
        font.draw(batch, fireUnitCounter.count.toString(), appWidth - panelWidth + panelWidth/4*1,appHeight-64f);
        batch.draw(waterUnitCounter.iconTexture, appWidth - panelWidth + panelWidth/4*2, appHeight-64f)
        font.draw(batch, waterUnitCounter.count.toString(), appWidth - panelWidth + panelWidth/4*2,appHeight-64f);
        batch.draw(grassUnitCounter.iconTexture, appWidth - panelWidth + panelWidth/4*3, appHeight-64f)
        font.draw(batch, grassUnitCounter.count.toString(), appWidth - panelWidth + panelWidth/4*3,appHeight-64f);
    }

    override fun dispose() {
        availableUnitCounter.iconTexture.dispose()
        fireUnitCounter.iconTexture.dispose()
        waterUnitCounter.iconTexture.dispose()
        grassUnitCounter.iconTexture.dispose()
    }
}
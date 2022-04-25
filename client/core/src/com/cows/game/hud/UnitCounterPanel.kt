package com.cows.game.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.cows.game.Application
import com.cows.game.enums.UnitType
import com.cows.game.managers.RoundManager
import com.cows.game.roundSimulation.rawJsonData.JsonUnit
import com.cows.game.views.Renderable

class UnitCounterPanel(availableUnits:Int):Renderable(){
    private val availableUnitCounter = UnitCounter(UnitType.NONE, availableUnits)
    private val fireUnitCounter = UnitCounter(UnitType.FIRE, 0)
    private val waterUnitCounter = UnitCounter(UnitType.WATER, 0)
    private val grassUnitCounter = UnitCounter(UnitType.GRASS, 0)

    var generator: FreeTypeFontGenerator = FreeTypeFontGenerator(Gdx.files.internal("Fonts/pokemon_pixel_font.ttf"))
    var headerParameter: FreeTypeFontGenerator.FreeTypeFontParameter
    var headerFont:BitmapFont
    var font:BitmapFont

    var hideUnits = false

    init {
        headerParameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        headerParameter.size = 60
        headerFont = generator.generateFont(headerParameter)
        headerParameter.size = 30
        font = generator.generateFont(headerParameter)

    }

    fun getJsonUnitList(): List<JsonUnit> {
        val fireUnit = RoundManager.gameStatus!!.availableUnits.fireUnit
        val waterUnit = RoundManager.gameStatus!!.availableUnits.waterUnit
        val grassUnit = RoundManager.gameStatus!!.availableUnits.grassUnit
        val units = mutableListOf<JsonUnit>()
        repeat(fireUnitCounter.count) { units.add(JsonUnit(null, UnitType.FIRE, fireUnit.level, fireUnit.movementSpeed, fireUnit.health )) }
        repeat(waterUnitCounter.count) { units.add(JsonUnit(null, UnitType.WATER, waterUnit.level, waterUnit.movementSpeed, waterUnit.health )) }
        repeat(grassUnitCounter.count) { units.add(JsonUnit(null, UnitType.GRASS, grassUnit.level, grassUnit.movementSpeed, grassUnit.health)) }
        return units
    }

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
        // This could be simplified if we put all UnitCounters in a list, and just loop through them
        val panelStartX =  Application.WIDTH - ActionPanel.WIDTH + ActionPanel.PADDING
        val headerStartY = Application.HEIGHT - ActionPanel.PADDING
        val availableCoinTextX = panelStartX + 60
        val availableCoinTextY = headerStartY - 5

        headerFont.draw(batch, availableUnitCounter.count.toString(), availableCoinTextX, availableCoinTextY);

        if (hideUnits) return

        font.draw(batch, fireUnitCounter.count.toString(), panelStartX + 85f, ActionPanel.ACTION_HEIGHT-109f + 20f);
        font.draw(batch, waterUnitCounter.count.toString(), panelStartX + 85f, ActionPanel.ACTION_HEIGHT-109f*2f - ActionPanel.UNIT_MARGIN*1f + 20f);
        font.draw(batch, grassUnitCounter.count.toString(), panelStartX + 85f, ActionPanel.ACTION_HEIGHT-109f*3f - ActionPanel.UNIT_MARGIN*2f + 20f);


    }

    override fun dispose() {
        availableUnitCounter.iconTexture.dispose()
        fireUnitCounter.iconTexture.dispose()
        waterUnitCounter.iconTexture.dispose()
        grassUnitCounter.iconTexture.dispose()
        generator.dispose()
    }
}
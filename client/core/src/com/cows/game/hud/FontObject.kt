package com.cows.game.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Vector2
import com.cows.game.Application
import com.cows.game.Redux
import com.cows.game.enums.UnitType
import com.cows.game.roundSimulation.rawJsonData.JsonUnit
import com.cows.game.views.Renderable

class FontObject(var text:String, fontSize:Int, var position:Vector2):Renderable() {

    var generator: FreeTypeFontGenerator = FreeTypeFontGenerator(Gdx.files.internal("Fonts/pokemon_pixel_font.ttf"))
    var headerParameter: FreeTypeFontGenerator.FreeTypeFontParameter
    var font: BitmapFont


    init {
        headerParameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        headerParameter.size = fontSize
        font = generator.generateFont(headerParameter)

    }

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        font.draw(batch, text, position.x, position.y);
    }

    override fun dispose() {
        generator.dispose()
    }
}
package com.cows.game.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Vector2
import com.cows.game.Application
import com.cows.game.enums.UnitType
import com.cows.game.roundSimulation.rawJsonData.JsonUnit
import com.cows.game.views.Renderable

data class Color(
    var red: Float,
    var blue: Float,
    var green: Float,
    var alpha: Float
)

class FontObject(var text:String, fontSize:Int, var position:Vector2):Renderable() {
    constructor(text:String, fontSize:Int) : this(text, fontSize, Vector2(0f,0f)) {}
    constructor(text: String, fontSize: Int, color: Color) : this(text, fontSize, Vector2(0f,0f)) {
        font.setColor(color.red, color.green, color.blue, color.alpha)
    }
    constructor(text: String, fontSize: Int, position: Vector2, color: Color) : this(text, fontSize, position) {
        font.setColor(color.red, color.green, color.blue, color.alpha)
    }

    var generator: FreeTypeFontGenerator = FreeTypeFontGenerator(Gdx.files.internal("Fonts/pokemon_pixel_font.ttf"))
    var headerParameter: FreeTypeFontGenerator.FreeTypeFontParameter
    var font: BitmapFont

    private var layout: GlyphLayout = GlyphLayout()


    init {
        headerParameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        headerParameter.size = fontSize
        font = generator.generateFont(headerParameter)
        layout.setText(font, text)
    }

    fun getFontWidth(): Float {
        return layout.width
    }

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        font.draw(batch, text, position.x, position.y);
    }

    override fun dispose() {
        generator.dispose()
    }
}
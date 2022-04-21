package com.cows.game.hud

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Skin


class TextField(skin: Skin) : Actor() {
    private var index = 0
    private var elapsed = 0f
    private val font: BitmapFont
    private val text = StringBuilder()
    override fun act(delta: Float) {
        // Just pretend a character is typed every .2 second
        elapsed += delta
        if (elapsed > 0.2f) {
            elapsed = 0f
            text.append(TEMPLATE[index++ % TEMPLATE.length])
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        font.color = Color.WHITE

        // Try to fit the full text, if after measuring it it turns out to be too long,
        // skip the first character and try again
        var textToDisplay: String? = text.toString()
        for (i in 0 until text.toString().length) {
            textToDisplay = text.toString().substring(i)
            val glyphLayout = GlyphLayout(font, textToDisplay)
            if (glyphLayout.width < width) {
                break
            }
        }
        font.draw(batch, textToDisplay, x, y + font.lineHeight)
    }

    companion object {
        private const val TEMPLATE = "The quick red fox jumps over the lazy brown dog. "
    }

    init {
        font = skin.getFont("default-font")
        width = 100f
        height = font.lineHeight
    }
}
package com.cows.game.hud

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.cows.game.Application
import com.cows.game.enums.GameState
import com.cows.game.managers.GameStateManager
import com.cows.game.serverConnection.ServerConnection
import com.cows.game.views.Renderable
import kotlinx.coroutines.runBlocking


class CreateGameMenu(private val onBackButton: () -> Unit): Renderable() {
    private val goBackBtn = Button("Buttons/back-btn.png", Vector2(416f, 100f)){onBackButton.invoke()}
    private val backgroundImg = Sprite(Texture("HUD/StartScreen/startscreen.png"))
    private var numbers: List<Int> = mutableListOf()// = gameCode.map { it.digitToInt() }
    private var gameCodeXPosition = listOf<Float>(5f, 155f, 305f, 455f, 605f, 5f, 155f, 305f, 455f, 605f)
    private var gameCodeYPosition = listOf<Float>(Application.HEIGHT-120f, Application.HEIGHT-300f)
    private var joinGameCode = mutableListOf<Sprite>()
    private val loadingText = Texture("HUD/StartScreen/loadingText.png")
//    (
//        Sprite(Texture("HUD/StartScreen/number"+ numbers[0] +".png")),
//        Sprite(Texture("HUD/StartScreen/number"+ numbers[1] +".png")),
//        Sprite(Texture("HUD/StartScreen/number"+ numbers[2] +".png")),
//        Sprite(Texture("HUD/StartScreen/number"+ numbers[3] +".png")),
//        Sprite(Texture("HUD/StartScreen/number"+ numbers[4] +".png"))
//    )

    init {
        println(numbers)
        joinGameCode.forEachIndexed { index, gameCode ->  gameCode.setPosition(gameCodeXPosition[index], gameCodeYPosition[0]-120)}
    }

    fun setGameCode(code: String) {
        numbers = code.map { it.digitToInt() }
    }

    fun setNumbersCasedOnCode() {
        joinGameCode = mutableListOf<Sprite>(
            Sprite(Texture("HUD/StartScreen/number"+ numbers[0] +".png")),
            Sprite(Texture("HUD/StartScreen/number"+ numbers[1] +".png")),
            Sprite(Texture("HUD/StartScreen/number"+ numbers[2] +".png")),
            Sprite(Texture("HUD/StartScreen/number"+ numbers[3] +".png")),
            Sprite(Texture("HUD/StartScreen/number"+ numbers[4] +".png"))
        )
        joinGameCode.forEachIndexed { index, gameCode ->  gameCode.setPosition(gameCodeXPosition[index], gameCodeYPosition[0]-120)}
    }

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        backgroundImg.draw(batch)
        if (numbers.isNotEmpty() && joinGameCode.isEmpty()) setNumbersCasedOnCode()

        if (numbers.isEmpty()) batch.draw(loadingText, 0f, 184f)
        else joinGameCode.forEach { sprite -> sprite.draw(batch)  }
    }

    override fun dispose() {
        joinGameCode.forEach { sprite -> sprite.texture.dispose()}
        backgroundImg.texture.dispose()
    }

    override fun die() {
        super.die()
        goBackBtn.die()
    }


}
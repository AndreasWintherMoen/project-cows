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

class JoinGameMenu(private val onJoinGame: (code: String) -> Unit, private val onBackButton: () -> Unit): Renderable(){
    private val backgroundImg = Sprite(Texture("HUD/StartScreen/background-clean2.png"))
    private var submitGameCodeBtn: Button? = Button("Buttons/enter-btn.png", Vector2(813f, 221f)){joinGame()}
    private var waitingeBtn: Button = Button("Buttons/waiting-button.png", Vector2(813f, 221f))
    private var goBackBtn: Button? = Button("Buttons/back-btn.png", Vector2(17f, 221f)){onBackButton.invoke()}
    private var gameCodeXPosition = listOf<Float>(313f, 403f, 493f,  583f, 673f, 763f , 583f, 943f, 1033f, 1123f)
    private var gameCodeYPosition = listOf<Float>(Application.HEIGHT-159f, 133f)
    val numbers = mutableListOf<Int>(0,0,0,0,0)
    private val joinGameCode = mutableListOf<Sprite>(
        Sprite(Texture("HUD/StartScreen/number"+ numbers[0] +".png")),
        Sprite(Texture("HUD/StartScreen/number"+ numbers[1] +".png")),
        Sprite(Texture("HUD/StartScreen/number"+ numbers[2] +".png")),
        Sprite(Texture("HUD/StartScreen/number"+ numbers[3] +".png")),
        Sprite(Texture("HUD/StartScreen/number"+ numbers[4] +".png"))
    )
    private val buttons = listOf<Button>(
        Button("HUD/StartScreen/arrow-up.png", Vector2(gameCodeXPosition[0], gameCodeYPosition[0])){ increment(0) },
        Button("HUD/StartScreen/arrow-up.png", Vector2(gameCodeXPosition[1], gameCodeYPosition[0])){ increment(1) },
        Button("HUD/StartScreen/arrow-up.png", Vector2(gameCodeXPosition[2], gameCodeYPosition[0])){ increment(2) },
        Button("HUD/StartScreen/arrow-up.png", Vector2(gameCodeXPosition[3], gameCodeYPosition[0])){ increment(3) },
        Button("HUD/StartScreen/arrow-up.png", Vector2(gameCodeXPosition[4], gameCodeYPosition[0])){ increment(4) },
        Button("HUD/StartScreen/arrow-down.png", Vector2(gameCodeXPosition[0], gameCodeYPosition[1])){ decrement(0) },
        Button("HUD/StartScreen/arrow-down.png", Vector2(gameCodeXPosition[1], gameCodeYPosition[1])){ decrement(1) },
        Button("HUD/StartScreen/arrow-down.png", Vector2(gameCodeXPosition[2], gameCodeYPosition[1])){ decrement(2) },
        Button("HUD/StartScreen/arrow-down.png", Vector2(gameCodeXPosition[3], gameCodeYPosition[1])){ decrement(3) },
        Button("HUD/StartScreen/arrow-down.png", Vector2(gameCodeXPosition[4], gameCodeYPosition[1])){ decrement(4) }
    )

    init {
        waitingeBtn.hide = true
    }

    fun increment(index:Int){
        numbers[index] = (numbers[index] + 11) % 10
        joinGameCode[index] = Sprite(Texture("HUD/StartScreen/number"+ numbers[index] +".png"))
        joinGameCode[index].setPosition(gameCodeXPosition[index], gameCodeYPosition[0]-120)
    }

    fun decrement(index:Int){
        numbers[index] = (numbers[index] + 9) % 10
        joinGameCode[index] = Sprite(Texture("HUD/StartScreen/number"+ numbers[index] +".png"))
        joinGameCode[index].setPosition(gameCodeXPosition[index], gameCodeYPosition[0]-120)
    }

    init {
        joinGameCode.forEachIndexed { index, gameCode ->  gameCode.setPosition(gameCodeXPosition[index], gameCodeYPosition[0]-120)}
    }

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        backgroundImg.draw(batch)
        joinGameCode.forEach { sprite -> sprite.draw(batch)  }
    }

    override fun dispose() {
        joinGameCode.forEach { number -> number.texture.dispose() }
        backgroundImg.texture.dispose()
    }

    override fun die() {
        super.die()
        submitGameCodeBtn?.die()
        submitGameCodeBtn = null
        goBackBtn?.die()
        goBackBtn = null
        buttons.forEach { it.die() }
    }

    private fun joinGame() {
        waitingeBtn.hide = false
        submitGameCodeBtn?.hide = true
        val joinCode = numbers.joinToString("")
        onJoinGame.invoke(joinCode)
    }

    fun onWrongCode() {
        waitingeBtn.hide = true
        submitGameCodeBtn?.hide = false
    }
}
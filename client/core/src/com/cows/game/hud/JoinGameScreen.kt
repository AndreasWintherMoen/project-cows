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

class JoinGameScreen(): Renderable(){
    private val submitGameCodeBtn = Button("Buttons/enterBtn.png", Vector2(725f, Application.HEIGHT-300f)){bar()}
    private val goBackBtn = Button("Buttons/enterBtn.png", Vector2(125f, 200f)){goBackToStartMenu()}
    private var gameCodeXPosition = listOf<Float>(5f, 155f, 305f, 455f, 605f, 5f, 155f, 305f, 455f, 605f)
    private var gameCodeYPosition = listOf<Float>(Application.HEIGHT-120f, Application.HEIGHT-300f)
    val numbers = mutableListOf<Int>(0,0,0,0,0)
    private val joinGameCode = mutableListOf<Sprite>(
        Sprite(Texture("HUD/StartScreen/number"+ numbers[0] +".png")),
        Sprite(Texture("HUD/StartScreen/number"+ numbers[1] +".png")),
        Sprite(Texture("HUD/StartScreen/number"+ numbers[2] +".png")),
        Sprite(Texture("HUD/StartScreen/number"+ numbers[3] +".png")),
        Sprite(Texture("HUD/StartScreen/number"+ numbers[4] +".png"))
    )
    private val buttons = listOf<Button>(
        Button("HUD/StartScreen/arrowUp.png", Vector2(gameCodeXPosition[0], gameCodeYPosition[0])){ increment(0) },
        Button("HUD/StartScreen/arrowUp.png", Vector2(gameCodeXPosition[1], gameCodeYPosition[0])){ increment(1) },
        Button("HUD/StartScreen/arrowUp.png", Vector2(gameCodeXPosition[2], gameCodeYPosition[0])){ increment(2) },
        Button("HUD/StartScreen/arrowUp.png", Vector2(gameCodeXPosition[3], gameCodeYPosition[0])){ increment(3) },
        Button("HUD/StartScreen/arrowUp.png", Vector2(gameCodeXPosition[4], gameCodeYPosition[0])){ increment(4) },
        Button("HUD/StartScreen/arrowDown.png", Vector2(gameCodeXPosition[5], gameCodeYPosition[1])){ decrement(0) },
        Button("HUD/StartScreen/arrowDown.png", Vector2(gameCodeXPosition[6], gameCodeYPosition[1])){ decrement(1) },
        Button("HUD/StartScreen/arrowDown.png", Vector2(gameCodeXPosition[7], gameCodeYPosition[1])){ decrement(2) },
        Button("HUD/StartScreen/arrowDown.png", Vector2(gameCodeXPosition[8], gameCodeYPosition[1])){ decrement(3) },
        Button("HUD/StartScreen/arrowDown.png", Vector2(gameCodeXPosition[9], gameCodeYPosition[1])){ decrement(4) }
    )

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
        joinGameCode.forEach { sprite -> sprite.draw(batch)  }
    }

    override fun dispose() {

    }

    override fun die() {
        super.die()
        submitGameCodeBtn.die()
        goBackBtn.die()
        buttons.forEach { it.die() }
    }

    fun goBackToStartMenu(){
        this.hide
    }

    private fun joinGame() {
        runBlocking {
            val joinCode = numbers.joinToString("")
            ServerConnection.joinGame(joinCode)
            die()
            GameStateManager.currentGameState = GameState.PLANNING_DEFENSE
        }
    }
}
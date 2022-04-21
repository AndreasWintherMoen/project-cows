package com.cows.game.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextArea
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.cows.game.Application
import com.cows.game.managers.Renderer
import com.cows.game.serverConnection.ServerConnection
import com.cows.game.views.Renderable


class StartMenu(): Renderable() {
    private val sprite = Sprite(Texture("HUD/StartScreen/startscreen.png"))
    private val joinGameBtn = Button("Buttons/joinbutton.png", Vector2(725f, Application.HEIGHT-300f)){println("buttonClick")}
    private val createGameBtn = Button("Buttons/start-button.png", Vector2(700f, Application.HEIGHT-400f)){createGame()}

    private var gameCodeXPosition = listOf<Float>(5f, 155f, 305f, 455f, 605f, 5f, 155f, 305f, 455f, 605f)
    private var gameCodeYPosition = listOf<Float>(Application.HEIGHT-120f, Application.HEIGHT-300f)
    private var numbers = mutableListOf<Int>(0,0,0,0,0)
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
        sprite.setSize(Application.WIDTH, Application.HEIGHT)
    }

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        sprite.draw(batch)

        joinGameCode.forEach { sprite -> sprite.draw(batch)  }
    }

    override fun dispose() {
        sprite.texture.dispose()
    }

    override fun die() {
        super.die()
        joinGameBtn.die()
        createGameBtn.die()
    }

    suspend fun createGame(){

    }
}
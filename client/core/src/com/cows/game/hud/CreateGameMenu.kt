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


class CreateGameMenu(private val gameCode: String, private val onBackButton: () -> Unit): Renderable() {
    private val goBackBtn = Button("Buttons/enterBtn.png", Vector2(125f, 100f)){onBackButton.invoke()}
    private val numbers = gameCode.map { it.digitToInt() }
    private var gameCodeXPosition = listOf<Float>(5f, 155f, 305f, 455f, 605f, 5f, 155f, 305f, 455f, 605f)
    private var gameCodeYPosition = listOf<Float>(Application.HEIGHT-120f, Application.HEIGHT-300f)
    private val joinGameCode = mutableListOf<Sprite>(
        Sprite(Texture("HUD/StartScreen/number"+ numbers[0] +".png")),
        Sprite(Texture("HUD/StartScreen/number"+ numbers[1] +".png")),
        Sprite(Texture("HUD/StartScreen/number"+ numbers[2] +".png")),
        Sprite(Texture("HUD/StartScreen/number"+ numbers[3] +".png")),
        Sprite(Texture("HUD/StartScreen/number"+ numbers[4] +".png"))
    )

    init {
        println(numbers)
        joinGameCode.forEachIndexed { index, gameCode ->  gameCode.setPosition(gameCodeXPosition[index], gameCodeYPosition[0]-120)}
    }

    suspend fun foo() {
        ServerConnection.connectToActiveGame()
        GameStateManager.currentGameState = GameState.PLANNING_ATTACK
    }
    override fun render(batch: SpriteBatch, deltaTime: Float) {
        joinGameCode.forEach { sprite -> sprite.draw(batch)  }
    }

    override fun dispose() {
        joinGameCode.forEach { sprite -> sprite.texture.dispose()}
    }

    override fun die() {
        super.die()
        goBackBtn.die()
    }


}
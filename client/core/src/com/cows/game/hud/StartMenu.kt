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
import com.cows.game.enums.GameState
import com.cows.game.managers.GameStateManager
import com.cows.game.managers.Renderer
import com.cows.game.serverConnection.ServerConnection
import com.cows.game.views.Renderable
import kotlinx.coroutines.runBlocking

class StartMenu(private val onJoinGameButton: () -> Unit, private val onCreateGameButton: () -> Unit): Renderable() {
    //Start menu
    private val backgroundImg = Sprite(Texture("HUD/StartScreen/startscreen.png"))
    private val joinGameBtn = Button("Buttons/join-btn.png", Vector2(555f, 233f)){showJoinGameMenu()}
    private val createGameBtn = Button("Buttons/create-btn.png", Vector2(280f, 233f), {showCreateGameMenu()}, "select-action.wav")

    init {
        backgroundImg.setSize(Application.WIDTH, Application.HEIGHT)
    }

    override fun render(batch: SpriteBatch, deltaTime: Float) {
        backgroundImg.draw(batch)
    }

    override fun dispose() {
        backgroundImg.texture.dispose()
    }

    override fun die() {
        super.die()
        joinGameBtn.die()
        createGameBtn.die()
    }

    private fun showCreateGameMenu(){
        this.hide = true
        joinGameBtn.hide = true
        createGameBtn.hide = true
        onCreateGameButton.invoke()
    }

    private fun showJoinGameMenu(){
        this.hide = true
        joinGameBtn.hide = true
        createGameBtn.hide = true
        onJoinGameButton.invoke()
    }

}
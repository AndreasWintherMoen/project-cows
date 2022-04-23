package com.cows.game.managers

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Texture
import com.cows.game.enums.GameState
import com.cows.game.enums.UnitType
import com.cows.game.gameState.GameStateSubscriber

object MusicPlayer: GameStateSubscriber() {
    private var music: Music? = null

    fun play() {
        println("PLASDAYSD")
        stopMusic()
        music = Gdx.audio.newMusic(Gdx.files.internal("Sound/intro.mp3"))
        music?.play()
        music?.setVolume(0.2f)
        music?.setLooping(true)
    }

    fun changeMusic(filename:String) {
        stopMusic()
        music = Gdx.audio.newMusic(Gdx.files.internal("Sound/$filename"))
        music?.play()
        music?.setVolume(0.2f)
        music?.setLooping(true)
    }

    fun stopMusic() {
        music?.stop()
    }

    override fun onChangeGameState(oldGameState: GameState, newGameState: GameState) {
        when (newGameState) {
            GameState.ACTIVE_GAME -> {
                changeMusic("battle.mp3")
            }
            GameState.PLANNING_ATTACK -> {
                changeMusic("planning.mp3")
            }
            GameState.PLANNING_DEFENSE -> {
                changeMusic("planning.mp3")
            }
            GameState.START_MENU -> {
                changeMusic("intro.mp3")
            }
        }
    }
}
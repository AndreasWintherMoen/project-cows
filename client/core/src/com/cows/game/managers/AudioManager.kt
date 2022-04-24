package com.cows.game.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.cows.game.enums.GameState
import com.cows.game.gameState.GameStateSubscriber

object AudioManager: GameStateSubscriber() {
    private var backgroundMusic: Music? = null
    private var soundEffect: Sound? = null

    fun init() {}

    init {
        playMusic("Sound/intro.mp3")
    }

    private fun playMusic(filePath: String, volume: Float = 1f, isLooping: Boolean = true) {
        backgroundMusic?.stop()
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(filePath))
        backgroundMusic?.play()
        backgroundMusic?.volume = volume
        backgroundMusic?.isLooping = isLooping
    }

    fun playSoundEffect(filePath: String, volume: Float = 1f) {
        soundEffect?.stop()
        soundEffect = Gdx.audio.newSound(Gdx.files.internal(filePath))
        val id = soundEffect?.play()
        id?.let {
            soundEffect?.setVolume(id, volume)
        }
    }

    override fun onChangeGameState(oldGameState: GameState, newGameState: GameState) {
        when (newGameState) {
            GameState.ACTIVE_GAME -> {
                playMusic("Sound/battle.mp3")
            }
            GameState.PLANNING_ATTACK -> {
                playMusic("Sound/planning.mp3")
            }
            GameState.PLANNING_DEFENSE -> {
                playMusic("Sound/planning.mp3")
            }
            GameState.START_MENU -> {
                playMusic("Sound/intro.mp3")
            }
            else -> { println("Could not find game state $newGameState in AudioManager::onChangeGameState") }
        }
    }
}
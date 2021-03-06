package com.cows.game.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.cows.game.enums.GameState
import com.cows.game.gameState.GameStateSubscriber

object AudioManager: GameStateSubscriber() {
    private var backgroundMusic: Music? = null
    private var soundEffect: Sound? = null
    var mute = false
        set(value) {
            if (value) {
                muteAllSound()
            } else {
                unmuteAllSound()
            }
            field = value
        }

    fun init() {}

    init {
        playMusic("Sound/intro-2.mp3")
    }

    private fun stopBackgroundMusic() {
        backgroundMusic?.stop()
        backgroundMusic = null
    }

    private fun stopSoundEffect() {
        soundEffect?.stop()
        soundEffect = null
    }

    private fun stopAllSound() {
        stopBackgroundMusic()
        stopSoundEffect()
    }

    private fun muteAllSound() {
        soundEffect?.stop()
        backgroundMusic?.volume = 0f
    }

    private fun unmuteAllSound() {
        backgroundMusic?.volume = 0.5f
    }

    fun playMusic(filePath: String, volume: Float = 1f, isLooping: Boolean = true) {
        if (mute) return
        backgroundMusic?.stop()
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(filePath))
        backgroundMusic?.play()
        backgroundMusic?.volume = volume
        backgroundMusic?.isLooping = isLooping
    }

    fun playSoundEffect(filePath: String, volume: Float = 1f) {
        if (mute) return
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
                playMusic("Sound/battle.mp3", 0.5f)
            }
            GameState.PLANNING_ATTACK -> {
                playMusic("Sound/planning.mp3", 0.5f)
            }
            GameState.PLANNING_DEFENSE -> {
                playMusic("Sound/planning.mp3", 0.5f)
            }
            GameState.START_MENU -> {
                playMusic("Sound/intro-2.mp3", 0.5f)
            }
            else -> { println("Could not find game state $newGameState in AudioManager::onChangeGameState") }
        }
    }
}
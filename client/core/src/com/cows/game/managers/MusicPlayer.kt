package com.cows.game.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music

object MusicPlayer {
    private var music: Music? = null

    fun play() {
        music = Gdx.audio.newMusic(Gdx.files.internal("Sound/intro.mp3"))
        music?.play()
        music?.setVolume(0.01f)
        music?.setLooping(true)
    }

    fun changeMusic(filename:String) {
        music = Gdx.audio.newMusic(Gdx.files.internal("Sound/$filename"))
        music?.play()
        music?.setVolume(0.2f)
        music?.setLooping(true)
    }

    fun stopMusic() {
        music?.stop()
    }
}
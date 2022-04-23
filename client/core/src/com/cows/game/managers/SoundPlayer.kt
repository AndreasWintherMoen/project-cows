package com.cows.game.managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

object SoundPlayer {
    var sound: Sound? = null

    fun play(file:String) {
        sound?.stop()
        sound = Gdx.audio.newSound(Gdx.files.internal("Sound/$file"))
        sound?.play()
    }
}
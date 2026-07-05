package proyecto.picobotella.repository

import android.content.Context
import android.media.MediaPlayer
import proyecto.picobotella.R

class AudioRepository(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var isPausedByUser = false

    fun startBackgroundMusic() {
        if (isPausedByUser) return

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.music_fondo).apply {
                isLooping = true
            }
        }

        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    fun pauseBackgroundMusic() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
            }
        }
    }

    fun resumeBackgroundMusic() {
        if (isPausedByUser) return
        mediaPlayer?.start()
    }

    fun toggleBackgroundMusic(): Boolean {
        isPausedByUser = !isPausedByUser

        if (isPausedByUser) {
            pauseBackgroundMusic()
        } else {
            startBackgroundMusic()
        }

        return !isPausedByUser
    }

    fun isMusicEnabled(): Boolean = !isPausedByUser

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPausedByUser = false
    }
}

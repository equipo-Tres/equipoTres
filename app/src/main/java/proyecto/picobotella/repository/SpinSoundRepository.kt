package proyecto.picobotella.repository

import android.content.Context
import android.media.MediaPlayer
import proyecto.picobotella.R

private const val MAX_PLAYS = 2

class SpinSoundRepository(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var playCount = 0

    fun startSpinSound() {
        stopSpinSound()
        playCount = 0
        mediaPlayer = MediaPlayer.create(context, R.raw.sound_bottle).apply {
            setVolume(1.0f, 1.0f)
            setOnCompletionListener { player ->
                playCount++
                if (playCount < MAX_PLAYS) {
                    player.seekTo(0)
                    player.start()
                } else {
                    stopSpinSound()
                }
            }
            start()
        }
    }

    fun stopSpinSound() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
        }
        mediaPlayer = null
    }

    fun release() {
        stopSpinSound()
    }
}

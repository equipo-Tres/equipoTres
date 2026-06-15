package proyecto.picobotella.data.repository

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
    //volver al home y restaurar audio (implementar)
    fun resumeBackgroundMusic() {
        if (isPausedByUser) return
        mediaPlayer?.start()
    }
    //es para despues implementar el boton de audio en toolbar
    fun toggleBackgroundMusic(): Boolean {
        isPausedByUser = !isPausedByUser

        if (isPausedByUser) {
            pauseBackgroundMusic()
        } else {
            startBackgroundMusic()
        }

        return !isPausedByUser
    }

    fun isMusicEnabled(): Boolean = !isPausedByUser //on/off
    //cerrar y liberar mediaplayer
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPausedByUser = false
    }
}

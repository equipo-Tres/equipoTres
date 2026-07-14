package proyecto.picobotella

import android.app.Application
import proyecto.picobotella.data.RetoDatabase
import proyecto.picobotella.repository.AudioRepository
import proyecto.picobotella.repository.PokemonRepository
import proyecto.picobotella.repository.RateRepository
import proyecto.picobotella.repository.RetoRepository
import proyecto.picobotella.repository.SpinSoundRepository
import proyecto.picobotella.webservice.PokemonApiUtils

class PicoBotellaApplication : Application() {

    val audioRepository by lazy {
        AudioRepository(this)
    }

    val spinSoundRepository by lazy {
        SpinSoundRepository(this)
    }

    val pokemonRepository by lazy {
        PokemonRepository(PokemonApiUtils.getPokemonApiService())
    }

    val rateRepository by lazy {
        RateRepository(this)
    }

    val retoRepository by lazy {
        RetoRepository(RetoDatabase.getDatabase(this).retoDao())
    }

    fun releaseMediaPlayers() {
        audioRepository.release()
        spinSoundRepository.release()
    }
}

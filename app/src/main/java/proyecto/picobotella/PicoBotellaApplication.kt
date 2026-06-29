package proyecto.picobotella

import android.app.Application
import proyecto.picobotella.data.local.RetoDatabase
import proyecto.picobotella.data.repository.AudioRepository
import proyecto.picobotella.data.repository.RateRepository
import proyecto.picobotella.data.repository.RetoRepository

class PicoBotellaApplication : Application() {

    val audioRepository by lazy {
        AudioRepository(this)
    }

    val rateRepository by lazy {
        RateRepository(this)
    }

    val retoRepository by lazy {
        RetoRepository(RetoDatabase.getDatabase(this).retoDao())
    }

    override fun onCreate() {
        super.onCreate()
    }
}

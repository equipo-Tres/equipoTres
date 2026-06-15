package proyecto.picobotella

import android.app.Application
import proyecto.picobotella.data.repository.RateRepository
import proyecto.picobotella.data.repository.AudioRepository
class PicoBotellaApplication : Application() {

    val audioRepository by lazy {
        AudioRepository(this)
    }

    val rateRepository by lazy {
        RateRepository(this)
    }

    override fun onCreate() {
        super.onCreate()
    }
}
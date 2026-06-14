package proyecto.picobotella

import android.app.Application
import proyecto.picobotella.data.repository.RateRepository

class PicoBotellaApplication : Application() {

    val rateRepository by lazy {
        RateRepository(this)
    }

    override fun onCreate() {
        super.onCreate()
    }
}
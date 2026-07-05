package proyecto.picobotella

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import proyecto.picobotella.data.RetoDatabase
import proyecto.picobotella.repository.AudioRepository
import proyecto.picobotella.repository.RateRepository
import proyecto.picobotella.repository.RetoRepository

class PicoBotellaApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

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
        applicationScope.launch {
            retoRepository.seedSampleRetosIfEmpty(
                listOf(
                    getString(R.string.sample_reto_1),
                    getString(R.string.sample_reto_2),
                    getString(R.string.sample_reto_3),
                    getString(R.string.sample_reto_4),
                    getString(R.string.sample_reto_5)
                )
            )
        }
    }
}

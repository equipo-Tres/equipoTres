package proyecto.picobotella.ui.home

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import proyecto.picobotella.data.repository.AudioRepository
import proyecto.picobotella.data.repository.RateRepository

class HomeViewModel(
    private val rateRepository: RateRepository,
    private val audioRepository: AudioRepository
) : ViewModel() {

    //criterio 3 hu 3
    private val _isMusicEnabled = MutableLiveData(audioRepository.isMusicEnabled())
    val isMusicEnabled: LiveData<Boolean> = _isMusicEnabled

    private val _openPlayStore = MutableLiveData<Intent>()
    val openPlayStore: LiveData<Intent> = _openPlayStore

    //criterio 4 hu3
    private val _navigateToInstructions = MutableLiveData<Boolean>()
    val navigateToInstructions: LiveData<Boolean> = _navigateToInstructions

    fun onHomeVisible() {
        audioRepository.startBackgroundMusic()
        _isMusicEnabled.value = audioRepository.isMusicEnabled() //sirve para que al volver al home vuelva a quedar el icono encendido
    }

    fun onHomeHidden(){
        audioRepository.pauseBackgroundMusic()
    }

    //criterio 3 hu3 on/of declarar funcion
    fun onAudioClicked(){
        val enabled = audioRepository.toggleBackgroundMusic()
        _isMusicEnabled.value = enabled
    }

    //criterio 4 hu3 nav inst
    fun onInfoClicked() {
        _navigateToInstructions.value = true
    }

    fun onStarClicked() {
        _openPlayStore.value = rateRepository.createPlayStoreIntent()
    }

    fun getWebFallbackIntent(): Intent {
        return rateRepository.createWebFallbackIntent()
    }
}